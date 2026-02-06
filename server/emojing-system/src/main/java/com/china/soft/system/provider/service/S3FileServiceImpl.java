package com.china.soft.system.provider.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.china.soft.system.config.S3Properties;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @Description: AWSS3操作持久类
 * @author MingYue
 * @Date 2023/10/9
 */
@ConditionalOnClass(S3Properties.class)
@Service
@Log4j2
public class S3FileServiceImpl {

    @Autowired
    private S3Properties s3Properties;

    private AmazonS3 amazonS3;

    @PostConstruct
    public void init() {
        log.info(s3Properties);
        amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(s3Properties.getAccessKey(), s3Properties.getSecretKey())))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        s3Properties.getEndpoint(),
                        s3Properties.getRegion()))
                .withPathStyleAccessEnabled(s3Properties.getPathStyleAccess())
                .withChunkedEncodingDisabled(true)
                .withClientConfiguration(new ClientConfiguration()
                        .withMaxConnections(s3Properties.getMaxConnections())
                        .withMaxErrorRetry(1))
                .build();
    }

    /**
     * 创建bucket
     * 注意：bucket name 不允许有特殊字符及大写字母
     *
     * @param bucketName bucket名称
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/CreateBucket">AWS API
     * Documentation</a>
     */
    @SneakyThrows
    public void createBucket(String bucketName) {
        if (!bucketName.toLowerCase().equals(bucketName)) {
            throw new RuntimeException("bucket name not allow upper case");
        }
        if (checkBucketExist(bucketName)) {
            log.info("bucket: {} 已经存在", bucketName);
            return;
        }
        amazonS3.createBucket((bucketName));
    }

    @SneakyThrows
    public boolean checkBucketExist(String bucketName) {
        return amazonS3.doesBucketExistV2(bucketName);
    }

    /**
     * 获取全部bucket
     * <p>
     *
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
     * API Documentation</a>
     */

    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    /**
     * 根据bucket获取bucket详情
     *
     * @param bucketName bucket名称
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
     * API Documentation</a>
     */

    @SneakyThrows
    public Optional<Bucket> getBucket(String bucketName) {
        return amazonS3.listBuckets().stream().filter(b -> b.getName().equals(bucketName)).findFirst();
    }

    /**
     * @param bucketName bucket名称
     * @see <a href=
     * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteBucket">AWS API
     * Documentation</a>
     */

    @SneakyThrows
    public void removeBucket(String bucketName) {
        amazonS3.deleteBucket(bucketName);
    }

    /**
     * 复制文件
     * @param bucketName
     * @param srcObjectName
     * @param tarObjectName
     */
    public void copyObject(String bucketName, String srcObjectName,String tarObjectName){
        amazonS3.copyObject(bucketName,srcObjectName,bucketName,tarObjectName);
    }

    /**
     * 上传文件，指定文件类型
     *
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param contextType 文件类型
     * @throws Exception
     */

    @SneakyThrows
    public void putObject(String bucketName, String objectName, InputStream stream,
                          String contextType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(stream.available());
        objectMetadata.setContentType(contextType);
        putObject(bucketName, objectName, stream, objectMetadata);
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     * @throws Exception
     */
    @SneakyThrows
    public void putObject(String bucketName, String objectName, InputStream stream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(stream.available());
        objectMetadata.setContentType("application/octet-stream");
        putObject(bucketName, objectName, stream, objectMetadata);
    }

    /**
     * 上传文件
     *
     * @param bucketName     bucket名称
     * @param objectName     文件名称
     * @param stream         文件流
     * @param objectMetadata 对象元数据
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/PutObject">AWS
     * API Documentation</a>
     */

    @SneakyThrows
    private PutObjectResult putObject(String bucketName, String objectName, InputStream stream,
                                      ObjectMetadata objectMetadata) {
        byte[] bytes = IOUtils.toByteArray(stream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // 上传
        return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);
    }


    /**
     * 判断object是否存在
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
     * API Documentation</a>
     */
    @SneakyThrows
    public boolean checkObjectExist(String bucketName, String objectName) {
        return amazonS3.doesObjectExist(bucketName, objectName);
    }


    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
     * API Documentation</a>
     */

    @SneakyThrows
    public S3Object getObject(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName, objectName);
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteObject">AWS
     * API Documentation</a>
     */

    @SneakyThrows
    public void deleteObject(String bucketName, String objectName) {
        amazonS3.deleteObject(bucketName, objectName);
    }

    /**
     * 大文件分段上传
     *
     * @param file        MultipartFile
     * @param bucketName  bucketName
     * @param objectName  objectName
     * @param minPartSize 每片大小，单位：字节（eg：5242880 <- 5m）
     */

    public void uploadMultipartFileByPart(MultipartFile file, String bucketName, String objectName,
                                          int minPartSize) {
        if (file.isEmpty()) {
            log.error("file is empty");
        }
        // 计算分片大小
        long size = file.getSize();
        // 得到总共的段数，和 分段后，每个段的开始上传的字节位置
        List<Long> positions = Collections.synchronizedList(new ArrayList<>());
        long filePosition = 0;
        while (filePosition < size) {
            positions.add(filePosition);
            filePosition += Math.min(minPartSize, (size - filePosition));
        }
        if (log.isDebugEnabled()) {
            log.debug("总大小：{}，分为{}段", size, positions.size());
        }

        // 创建一个列表保存所有分传的 PartETag, 在分段完成后会用到
        List<PartETag> partETags = Collections.synchronizedList(new ArrayList<>());

        // 第一步，初始化，声明下面将有一个 Multipart Upload
        // 设置文件类型
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());

        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName,
                objectName, metadata);
        InitiateMultipartUploadResult initResponse = this.initiateMultipartUpload(initRequest);
        if (log.isDebugEnabled()) {
            log.debug("开始上传");
        }
        //声明线程池
        ExecutorService exec = Executors.newFixedThreadPool(3);
        long begin = System.currentTimeMillis();
        try {
            // MultipartFile 转 File
            File toFile = multipartFileToFile(file);
            for (int i = 0; i < positions.size(); i++) {
                int finalI = i;
                exec.execute(() -> {
                    long time1 = System.currentTimeMillis();
                    UploadPartRequest uploadRequest = new UploadPartRequest()
                            .withBucketName(bucketName)
                            .withKey(objectName)
                            .withUploadId(initResponse.getUploadId())
                            .withPartNumber(finalI + 1)
                            .withFileOffset(positions.get(finalI))
                            .withFile(toFile)
                            .withPartSize(Math.min(minPartSize, (size - positions.get(finalI))));
                    // 第二步，上传分段，并把当前段的 PartETag 放到列表中
                    partETags.add(this.uploadPart(uploadRequest).getPartETag());
                    if (log.isDebugEnabled()) {
                        log.debug("第{}段上传耗时：{}", finalI + 1, (System.currentTimeMillis() - time1));
                    }
                });
            }
            //任务结束关闭线程池
            exec.shutdown();
            //判断线程池是否结束，不加会直接结束方法
            while (true) {
                if (exec.isTerminated()) {
                    break;
                }
            }

            // 第三步，完成上传，合并分段
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(
                    bucketName,
                    objectName,
                    initResponse.getUploadId(), partETags);
            this.completeMultipartUpload(compRequest);

            //删除本地缓存文件
            if (toFile != null && !toFile.delete()) {
                log.error("Failed to delete cache file");
            }
        } catch (Exception e) {
            this.abortMultipartUpload(
                    new AbortMultipartUploadRequest(bucketName, objectName,
                            initResponse.getUploadId()));
            log.error("Failed to upload, " + e.getMessage());
        }
        if (log.isDebugEnabled()) {
            log.debug("总上传耗时：{}", (System.currentTimeMillis() - begin));
        }
    }

    /**
     * 根据文件前置查询文件集合
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return S3ObjectSummary 列表
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListObjects">AWS
     * API Documentation</a>
     */

    @SneakyThrows
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix,
                                                       boolean recursive) {
        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        return new ArrayList<>(objectListing.getObjectSummaries());
    }

    /**
     * 查询文件版本
     *
     * @param bucketName bucket名称
     * @return S3ObjectSummary 列表
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListObjects">AWS
     * API Documentation</a>
     */

    @SneakyThrows
    public List<S3VersionSummary> getAllObjectsVersionsByPrefixV2(String bucketName,
                                                                  String objectName) {
        VersionListing versionListing = amazonS3.listVersions(bucketName, objectName);
        return new ArrayList<>(versionListing.getVersionSummaries());
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires    过期时间 <=7 单位天
     * @return url
     */

    @SneakyThrows
    public String generatePresignedUrl(String bucketName, String objectName, Integer expires) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, expires);
        URL url = amazonS3.generatePresignedUrl(bucketName, objectName, calendar.getTime());
        return url.toString();
    }

    /**
     * 开放链接，默认public没有设置访问权限
     * url 规则：${endPoint}/${bucketName}/${objectName}
     *
     * @param bucketName
     * @param objectName
     * @return
     */
    public String generatePublicUrl(String bucketName, String objectName) {
        return s3Properties.getEndpoint() + "/" + bucketName + "/" + objectName;
    }

    /**
     * 初始化，声明有一个Multipart Upload
     *
     * @param initRequest 初始化请求
     * @return 初始化返回
     */
    private InitiateMultipartUploadResult initiateMultipartUpload(
            InitiateMultipartUploadRequest initRequest) {
        return amazonS3.initiateMultipartUpload(initRequest);
    }

    /**
     * 上传分段
     *
     * @param uploadRequest 上传请求
     * @return 上传分段返回
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/UploadPart">AWS
     * API Documentation</a>
     */
    private UploadPartResult uploadPart(UploadPartRequest uploadRequest) {
        return amazonS3.uploadPart(uploadRequest);
    }

    /**
     * 分段合并
     *
     * @param compRequest 合并请求
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/CompleteMultipartUpload">AWS
     * API Documentation</a>
     */
    private CompleteMultipartUploadResult completeMultipartUpload(
            CompleteMultipartUploadRequest compRequest) {
        return amazonS3.completeMultipartUpload(compRequest);
    }

    /**
     * 中止分片上传
     *
     * @param uploadRequest 中止文件上传请求
     * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/AbortMultipartUpload">AWS
     * API Documentation</a>
     */
    private void abortMultipartUpload(AbortMultipartUploadRequest uploadRequest) {
        amazonS3.abortMultipartUpload(uploadRequest);
    }

    /**
     * MultipartFile 转 File
     */
    private File multipartFileToFile(MultipartFile file) throws Exception {
        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            //获取流文件
            OutputStream os = new FileOutputStream(toFile);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        }
        return toFile;
    }
}