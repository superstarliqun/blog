package com.china.soft.upload.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * description 抽象静态类，减少子类实现代码
 * 注释 {@link FileClientConfig} 是规定了配置参数只能是FileClientConfig或者是他的子类，这样可以安心使用配置类型中的参数
 * @author  MingYue
 * @version 1.0
 * @since   2025/4/2
 */
@Slf4j
public abstract class AbstractFileClient<Config extends FileClientConfig> implements FileClient {

    /** 配置编号  */
    private final Long id;

    /** 文件配置  */
    protected Config config;

    public AbstractFileClient(Long id, Config config) {
        this.id = id;
        this.config = config;
    }

    /** 初始化 */
    public final void init() {
        initializeHandle();
        log.debug("[init][配置({}) 初始化完成]", config);
    }

    /**
     * 由子类实现自定义初始化
     */
    protected abstract void initializeHandle();

    public final void refresh(Config config) {
        // 判断是否更新
        if (config.equals(this.config)) {
            return;
        }
        log.info("[refresh][配置({})发生变化，重新初始化]", config);
        this.config = config;
        // 初始化
        this.init();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String check(String fileHash, Long fileSize, String fileName) {
        System.out.println("获取文件hash值："+fileHash);
        return "";
    }

    @Override
    public String direct(MultipartFile file, String fileHash, String fileName) {
        return "";
    }

    @Override
    public String chunk(String uploadId, Integer chunkIndex, MultipartFile fileChunk, Integer totalChunks, String chunkHash) {
        return "";
    }

    @Override
    public String merge(String uploadId, String fileHash, String fileName) {
        return "";
    }

    @Override
    public String cleanup(String uploadId) {
        return "";
    }
}
