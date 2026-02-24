<template>
  <div class="profile-card">
    <div class="banner" />

    <div class="content-wrapper">
      <div class="avatar-container">
        <img src="https://pic1.zhimg.com/v2-5be04c5f2debd0260f86da2a324655bf_xl.jpg?source=32738c0c&needBackground=1"
          alt="Avatar" class="avatar">
      </div>
      <div class="info">
        <div class="head-container">
          <div class="name">
            <p>MingYue</p>
            <span class="handle">@superstarliqun</span>
          </div>
          <div class="main-actions">
            <button class="btn-follow"><span class="plus">+</span> Follow</button>
          </div>
        </div>
        <h3 class="section-title">About</h3>
        <p class="bio">
          Java 开发工程师。在后端领域不断探索，努力把每一行逻辑写得更清晰，在解决问题的过程中寻找代码的乐趣
        </p>
        <h3 class="section-title">Social accounts</h3>
        <ul class="accounts">
          <li @click="goToUrl('https://v.douyin.com/wDV7V6rq0a8/ 9@4.com :4pm')">
            <i class="iconfont icon-douyin" style="color: #d93153;" />
            <p>douyin</p>
          </li>
          <li @click="goToUrl('https://space.bilibili.com/352095550')">
            <i class="iconfont icon-blibli" style="color: #f77ca1;" />
            <p>blilibilil</p>
          </li>
          <li @click="goToUrl('https://github.com/superstarliqun')">
            <i class="iconfont icon-github1" />
            <p>github</p>
          </li>
          <li @click="goToUrl('https://www.zhihu.com/people/superstarliqun')">
            <i class="iconfont icon-shejiaotubiao" style="color: #0167ff;" />
            <p>zhihu</p>
          </li>
          <li @click="goToUrl('')">
            <i class="iconfont icon-email" style="color: #3a9de8;" />
            <p>Email</p>
          </li>
        </ul>
      </div>

    </div>
  </div>
</template>
<script>
export default {
  methods: {
    goToUrl(url) {
      window.open(url, '_blank')
    }
  }
}
</script>
<style scoped>
.profile-card {
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  z-index: 1;
}

.banner {
  height: 100px;
  background: linear-gradient(17deg, #ffafbd, #ffc3a0, #ebedee, #ffafbd, #f4f4f4);
  border-radius: 8px;
  box-shadow: var(--box-shadow);
  border: var(--style-border);
  z-index: 1;
}

.content-wrapper {
  padding: 40px 16px 16px;
  position: relative;
  border-radius: 8px;
  background: var(--card-background);
  box-shadow: var(--box-shadow);
  border: var(--style-border);
}

.avatar-container {
  position: absolute;
  top: -46px;
  left: 24px;
  z-index: 2;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 2px solid #fff;
  object-fit: cover;
  user-select: none;
  -webkit-user-drag: none;
  pointer-events: none;
}

.head-container {
  display: flex;
  justify-content: space-between;

}

.action-buttons {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  padding-top: 12px;
  gap: 12px;
}

.btn-edit {
  border: 1px solid #e0e0e0;
  background: white;
  padding: 6px 12px;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
}

.main-actions {
  display: flex;
  gap: 8px;
  align-items: flex-start;
}

.btn-follow {
  background: #111;
  color: #fff;
  border: none;
  padding: 6px 8px;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 3px 4px 11px 0px rgba(0, 0, 0, 0.3)
}

.name {
  font-size: 16px;
  line-height: 1.2;

  p {
    font-weight: 600;
  }
}

.handle {
  color: #888;
  margin: 4px 0 16px;
  font-size: 12px;
}

.section-title {
  font-size: 12px;
  font-weight: bold;
  margin: 16px 0 4px 0;
}

.bio {
  font-size: 12px;
  color: #555;
  line-height: 1.5;
}

.stats {
  display: flex;
  justify-content: space-between;
  border-top: 1px solid #f0f0f0;
  padding-top: 20px;
}

.stat-item {
  display: flex;
  flex-direction: column;
}

.count {
  font-size: 22px;
  font-weight: 700;
  color: #111;
}

.label {
  font-size: 13px;
  color: #888;
  margin-top: 4px;
}

.info {
  font-size: 14px;

  .accounts {
    display: flex;
    justify-content: space-between;
    margin-top: 0.5rem;

    li {
      display: flex;
      flex-direction: column;
      gap: 4px;
      align-items: center;
      font-size: 10px;

      .iconfont {
        width: 30px;
        height: 30px;
        border: 0.6px solid #e2e2e2;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        font-size: 18px;
      }
    }
  }
}
</style>
