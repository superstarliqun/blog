<template>
  <div class="wrap">
    <div class="wrap-content">
      <div class="left">
        <div class="left-container">
          <div class="banner-container" style="position: relative;box-shadow:0 8px 16px -4px #2c2d300c;">
            <img class="left-banner" style="" src="https://p.zhheo.com/nI40Ii25391281766720093204.webp!blogimg_1600w">
            <div class="banner-text">
              <a href="#">用腾讯CoDesign打造专属SVG图标库</a>
              <span
                class="desc jianjie"
              >腾讯CoDesign支持用户上传自定义SVG图标文件，帮助设计团队建立专属图标库。通过简单的拖拽操作即可批量上传SVG文件，支持保留或去除颜色，实现图标资源的统一管理和高效调用。</span>
              <p class="banner-list-footer">
                <span># 2026-01-14 </span>
                <span>Java</span>
                <span class="icon-container"><i class="iconfont icon-huifu" />3</span>
                <span class="icon-container"><i class="iconfont icon-guankan" /> 1035</span>
              </p>
            </div>
          </div>
          <div class="hot-container">
            <p class="hot-title container-title">热门文章</p>
            <ul class="left-images-module">
              <li v-for="value in 3" :key="value">
                <img src="	https://p.zhheo.com/ic92k123391281765529373270.webp!cover" alt="">
                <div class="images-module-container">
                  <a href="#">热门标题中心控制呢</a>
                  <span class="desc">这篇文章介绍了如何在 macOS 上使用快捷指令结合 shell 命令来进行媒体文件的格式转换。文章首先讲解了安装 Homebrew 和 ffmpeg
                    的方法，使系统具备音视频处理能力；随后详细介绍了创建快速操作快捷指令的步骤，包括选择媒体类型、添加 shell 脚本、设置参数</span>
                  <p class="left-list-footer">
                    <span># 2026-01-14</span>
                    <span>Java</span>
                    <span class="icon-container"><i class="iconfont icon-huifu" />3</span>
                    <span class="icon-container"> <i class="iconfont icon-guankan" />1035</span>
                  </p>
                </div>
              </li>
            </ul>
          </div>
          <div class="new-container">
            <p class="new-title container-title">最新文章</p>
            <ul class="posts-container">
              <li v-for="item in posts" :key="item.id" class="posts-item">
                <img src="@/assets/img/posts.png" alt="">
                <div class="posts-container-right">
                  <p class="posts-title" @click="toDetails(item)"> {{ item.title }}</p>
                  <div class="posts-desc">{{ item.summary == null ? '这篇文章没有留下点什么' : item.summary }}</div>
                  <div class="posts-meta">
                    <p class="posts-meta-trends">
                      <span>{{ converter(item.category) }}</span>
                      <span><i class="iconfont icon-guankan" /> {{ item.views }}</span>
                      <span><i class="iconfont icon-huifu" /> {{ item.comments }}</span>
                      <span><i class="iconfont icon-icon" />{{ item.likes }}</span>
                    </p>
                  </div>
                </div>
              </li>
            </ul>
          </div>

        </div>
      </div>
      <!-- aside-content -->
      <div class="right">
        <div class="card-widget card-content">
          <div class="card-info-avatar is-center">
            <div class="author-info__top-group">
              <div class="author-info__sayhi"> 团队小组发动机
              </div>
            </div>
          </div>
          <div class="avatar-img-group">
            <div id="lottie_avatar" class="avatar-img">123</div>
            <div class="avatar-sticker">123</div>
          </div>
          <div class="author-info__description_group">
            <div class="author-info__description">这有关于<b>产品、设计、开发</b>相关的问题和看法，还有<b>文章翻译</b>和<b>分享</b>。</div>
            <div class="author-info__description">相信你可以在这里找到对你有用的<b>知识</b>和<b>教程</b>。</div>
          </div>
          <div class="author-info__bottom-group">
            <a class="author-info__bottom-group-left" href="/about">
              <div class="author-info__name">MingYue</div>
              <div class="author-info__desc">记录工作日常</div>
            </a>
            <div class="card-info-social-icons is-center">
              123
            </div>
          </div>
        </div>
        <div class="card-widget card-category">
          <div class="card-title">
            <span>分类</span>
            <router-link to="/category" class="card-title-more">
              更多</router-link>
          </div>
          <ul class="category-container">
            <li v-for="item in categories" :key="item.id">
              <div class="category-name">
                <img :src="replaceImg(item.id)" alt="">
                {{ item.categoryName }}
              </div>
              <span class="category-number">32篇</span>
            </li>
          </ul>
        </div>
        <div class="card-widget card-tag">
          <div class="card-title">
            <span>标签</span>
            <router-link to="/tags" class="card-title-more">
              更多</router-link>
          </div>
          <div class="tag-list">
            <span v-for="item in tags" :key="item.id" class="tag">
              <router-link :to="'/tags/' + item.tagsName">
                {{ item.tagsName }}
                <span class="tag-number">{{ item.number }}</span>
              </router-link>
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      posts: [], // 文章列表集合
      tags: [], // 标签列表集合
      categories: [] // 后期改用cats 分类列表集合
    }
  },
  mounted() {
    this.requestData()
  },
  methods: {
    requestData() {
      this.$get(this.$urls.getDefaultCategory).then((res) => {
        if (res.code === 0) {
          this.categories = res.data
        }
      })
      this.$get(this.$urls.getTagList).then((res) => {
        this.tags = res.data
      })
      this.$get(this.$urls.articlePage, this.searchForm, [1, 10]).then((res) => {
        this.posts = res.data.list
        console.log(this.posts)
      })
    },
    replaceImg(index) {
      return require(`@/assets/img/category/${index}.png`)
    },
    // 转化类型
    converter(id) {
      const target = this.categories.filter((item) => {
        return item.id === id
      })
      if (target.length > 0) {
        return target[0].categoryName
      } else {
        return null
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.wrap {
  margin: 20px;

  .wrap-content {
    max-width: 1440px;
    margin: 0 auto;
    display: flex;
    gap: 0.5rem;
    padding: 0 1.5rem;

    .left {
      width: calc(100% - 300px);
    }

    .right {
      flex: 1;
      display: flex;
      gap: 10px;
      flex-direction: column;
    }
  }
}

.jianjie {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;

  line-height: 1.6;
  max-height: calc(1.6em * 2);
  color: #d3d3d3;
}

.left {
  .left-container {
    display: flex;
    flex-direction: column;
    gap: 8px;

    .card-widget {
      box-shadow: 0 8px 16px -4px rgba(44, 45, 48, 0.0470588235);
      padding: 10px;
      border: var(--style-border);
      border-radius: 8px;
      background-color: var(--card-background);
      font-size: 16px;
      line-height: 2;
    }

    .left-banner {
      display: block;
      box-shadow: var(--box-shadow);
      width: 100%;
      height: 420px;
      object-fit: cover;
      border-radius: 8px;
      position: relative;
    }

    .banner-text {
      position: absolute;
      bottom: 0;
      left: 0;
      padding: 20px;
      color: white;
      display: flex;
      flex-direction: column;
      gap: 10px;
      font-size: 14px;
      line-height: 1;

      background: rgba(0, 0, 0, 0.4);
      backdrop-filter: blur(4px) saturate(180%);
      border-radius: 0 0 8px 8px;
      border-top: 1px solid rgba(127, 127, 127, 0.5);

      &::before {
        /* 注入 SVG 噪点数据 */
        background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
        /* 透明度必须极低，建议 0.03 - 0.08 */
        opacity: 0.05;
        pointer-events: none;
        border-radius: 8px;
      }

      .banner-list-footer {
        display: flex;
        gap: 8px;
        line-height: 1;
        color: #d3d3d3;

        .iconfont {
          font-size: 14px;
        }
      }

      a {
        color: white;
        text-decoration: none;
        cursor: default;
        outline: none;
        font-weight: 600;
        font-size: 34px;
      }
    }

    .left-text-module {
      font-size: 12px;
      color: #76788d;
      display: flex;
      flex-direction: column;
      gap: 12px;

      li {
        display: flex;
        flex-direction: column;
        gap: 4px;

        &:last-child {
          border-bottom: unset
        }

        a {
          color: black;
          font-size: 18px;
          font-weight: 600;
        }

        .desc {
          display: flex;
          gap: 8px;
        }

      }

    }

    .hot-container {
      padding: 16px 0;

      .left-images-module {
        font-size: 12px;
        color: #76788d;
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(270px, 1fr));
        gap: 16px;
        cursor: pointer;

        li {
          display: flex;
          flex-direction: column;
          // gap: 12px;
          background-color: #fff;
          box-shadow: 0 8px 16px -4px #2c2d300c;
          border-radius: 8px;

          .images-module-container {
            padding: 10px;
            display: flex;
            flex-direction: column;
            gap: 12px;
            font-size: 12px;
            justify-content: space-between;

            .desc {
              display: -webkit-box;
              -webkit-box-orient: vertical;
              -webkit-line-clamp: 2;
              line-clamp: 2;
              overflow: hidden;
              text-overflow: ellipsis;
              line-height: 1.6;
              max-height: 3.2em;
            }

            a {
              color: black;
              font-size: 18px;
              font-weight: 600;
            }

            .iconfont {
              font-size: 14px;
            }
          }

        }

        img {
          width: 100%;
          border-radius: 8px 8px 0 0;
        }
      }
    }

    .new-container {
      padding: 16px 0;

      .posts-container {
        display: flex;
        flex-direction: column;
        gap: 16px;

        .posts-container-right {
          flex: 1;
          display: flex;
          flex-direction: column;
          gap: 8px;
          z-index: 99999;
        }

        .posts-item {
          padding: 10px 18px;
          background-color: #fff;
          box-shadow: 0 8px 16px -4px #2c2d300c;
          font-size: 13px;
          color: #7d8397;
          border-radius: 8px;
          display: flex;
          gap: 18px;
          position: relative;
          z-index: 999;
          overflow: hidden;

          &::after {
            content: '';
            position: absolute;
            top: -4px;
            right: -3px;
            width: 400px;
            height: 130px;
            background-image: url(@/assets/img/test2.png);
            background-size: cover;
            background-position: top right;
            background-repeat: no-repeat;
            -webkit-mask-image: linear-gradient(to left, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0));
            mask-image: linear-gradient(to left, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0));

            z-index: 0; // 内容在上
            pointer-events: none;
          }

          img {
            width: 100px;
            border-radius: 18px;
          }

          .posts-title {
            font-size: 20px;
            font-weight: 600;
            color: black;
          }

          .posts-desc {
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            line-clamp: 2;
            overflow: hidden;
            text-overflow: ellipsis;
            line-height: 1rem;
            min-height: 1.5rem;
          }

          .posts-meta {
            .posts-meta-trends {
              &>span {
                display: inline-flex;
                align-items: center;
                vertical-align: middle;
                gap: 4px;
              }

              display: flex;
              gap: 8px;
              line-height: 1;

              .iconfont {
                font-size: 14px;
              }
            }
          }
        }
      }
    }

    // 每个容器的标题
    .container-title {
      align-items: center;
      font-weight: 600;
      font-size: 20px;
      margin-bottom: 8px;
    }
  }

  // 主页文章脚注
  .left-list-footer {
    display: flex;
    gap: 8px;
    align-items: center;
    line-height: 1;
    font-size: 14px;
  }
}

.right {
  .card-widget {
    box-shadow: 0 8px 16px -4px #2c2d300c;
    padding: 10px;
    border: 1px solid #e3e8f7;
    border-radius: 8px;
    background-color: #fff;
    font-size: 16px;
    line-height: 2;

    .card-title {
      display: flex;
      justify-content: space-between;
      align-items: center;

      :first-child {
        font-weight: 600;
      }

      .card-title-more {
        font-weight: unset;
        cursor: pointer;
        font-size: 14px;
        color: #959595;

        &::after {
          content: '\e615';
          font-family: 'iconfont';
          font-size: 14px;
          margin-left: 2px;
        }

        &:hover {
          color: #4b9bcb;
        }

        &>i {
          font-weight: 12px;
        }
      }
    }
  }

  .card-tag {
    .tag-list {
      display: flex;
      flex-wrap: wrap;
      gap: 8px 10px;
      margin-top: 10px;

      .tag {
        display: inline-block;

        span {
          display: flex;
          align-items: center;
          padding: 2px 10px;
          background-color: #f6f8fa;
          color: #4c4948;
          border-radius: 15px;
          font-size: 13px;
          transition: all 0.2s ease-in-out;
          cursor: pointer;

          &:hover {
            background-color: #49b1f5;
            color: #fff;

            .tag-number {
              background-color: #fff;
              color: #49b1f5;
            }
          }
        }

        .tag-number {
          margin-left: 5px;
          background-color: #a3abc1;
          color: #fff;
          font-size: 11px;
          height: 16px;
          min-width: 16px;
          padding: 0 5px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          transition: all 0.2s;
        }
      }
    }
  }

  .category-container {
    margin-top: 8px;
    font-size: 14px;
    display: flex;
    flex-direction: column;
    gap: 4px;

    li {
      line-height: 2;
      display: flex;
      justify-content: space-between;
      border: 1px solid #e7e9f0;
      padding: 4px 8px;
      border-radius: 4px;
      color: #191d22;
      cursor: pointer;

      &:hover {
        background-color: #f2f7fd;
      }

      img {
        width: 26px;
        /* 固定宽度 */
        height: 26px;
        /* 固定高度，保持 1:1 */
        object-fit: cover;
        /* 核心：防止图片拉伸，自动裁剪适配容器 */
        flex-shrink: 0;
        /* 防止在空间不足时图片被挤压 */
        border-radius: 4px;
        /* 可选：增加一点圆角美观 */
      }

      .category-number {
        color: #99a1af;

        &::after {
          content: '\e756';
          font-family: 'iconfont';
          font-size: 14px;
          margin-left: 6px;
          color: #99a1af;
        }
      }
    }

    .category-name {
      display: flex;
      gap: 12px;
      margin-left: 6px;
      align-items: center;
    }
  }

  // 设计右侧欢迎卡片
  .card-widget.card-content {
    background: linear-gradient(-25deg, #425AEF, #4259efdd);
    color: white;
    padding: 1rem 1.2rem;
    min-height: 320px;
    height: 320px;
    position: relative;
    user-select: none;
    display: flex;
    flex-direction: column;
    gap: 8px;
    position: relative;
    text-align: center;

    .author-info__description_group {
      display: none;
      opacity: 0;
    }

    // .author-info__sayhi {
    //   transition: all 0.3s ease-in-out;
    //   background: white;
    //   width: 20px;
    //   height: 20px;
    //   margin: 2px;
    //   transform-origin: center;
    //   /* 确保从中心缩放 */
    // }

    // .author-info__sayhi:hover {
    //   transform: scale(10);
    //   /* 放大到200px（20px * 10） */
    // }
  }
}
</style>
