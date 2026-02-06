<template>
  <div class="page">
    <header class="topbar">
      <h1 class="title">炫酷视频广场</h1>
      <div class="controls">
        <input v-model="query" placeholder="搜索视频、作者..." class="search" />
      </div>
    </header>

    <main class="stage">
      <div v-if="filtered.length === 0" class="empty">
        没有匹配的视频 — 尝试更换关键词
      </div>

      <div class="grid">
        <div
          v-for="video in filtered"
          :key="video.id"
          class="card"
          @click="open(video)"
          @mouseover="hoverId = video.id"
          @mouseleave="hoverId = null"
        >
          <div class="thumbWrap">
            <img :src="video.thumb" class="thumb" :alt="video.title" />
            <!-- hover preview video (muted autoplay) -->
            <video
              v-if="hoverId === video.id"
              :src="video.src"
              muted
              playsinline
              autoplay
              loop
              class="preview"
            ></video>

            <div class="badge">
              <svg viewBox="0 0 24 24">
                <path fill="currentColor" d="M8 5v14l11-7z" />
              </svg>
            </div>
            <div class="time">{{ video.duration }}</div>
          </div>

          <div class="meta">
            <div class="titleRow">
              <h3 class="vtitle">{{ video.title }}</h3>
              <span class="dot">•</span>
              <span class="author">{{ video.author }}</span>
            </div>
            <p class="desc">{{ video.description }}</p>
          </div>
        </div>
      </div>
    </main>

    <transition name="modal">
      <div v-if="player" class="modal" @click.self="close">
        <div class="playerCard">
          <div class="playerHeader">
            <h2>{{ player.title }}</h2>
            <button class="closeBtn" @click="close">×</button>
          </div>
          <video
            ref="videoEl"
            :src="player.src"
            controls
            autoplay
            class="bigVideo"
          ></video>
          <div class="playerInfo">
            <div>{{ player.author }}</div>
            <div class="tags">
              <span class="tag">炫酷</span>
              <span class="tag">高清</span>
              <span class="tag">示例</span>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const query = ref('')
const hoverId = ref(null)
const player = ref(null)
const videoEl = ref(null)

const sampleVideos = [
  {
    id: 1,
    title: 'Neon City - 夜色',
    author: 'Studio A',
    duration: '02:34',
    description: '赛博朋克风格短片，霓虹与雨。',
    thumb:
      'https://images.unsplash.com/photo-1499346030926-9a72daac6c63?q=80&w=1200&auto=format&fit=crop&ixlib=rb-4.0.3&s=02a7b2c3cab3d9f2f6b0985a3b0d3b2a',
    src: 'https://www.w3schools.com/html/mov_bbb.mp4'
  },
  {
    id: 2,
    title: 'Mountain Echoes',
    author: 'Nature Cam',
    duration: '04:12',
    description: '山谷回声与极致景观。',
    thumb:
      'https://images.unsplash.com/photo-1501785888041-af3ef285b470?q=80&w=1200&auto=format&fit=crop&ixlib=rb-4.0.3&s=5b7b3c6f6b3f8b9e9b1f6e0f6b0a2c3d',
    src: 'https://www.w3schools.com/html/mov_bbb.mp4'
  },
  {
    id: 3,
    title: 'Space Voyage',
    author: 'Orbital',
    duration: '03:20',
    description: '穿越星际的沉浸式画面。',
    thumb:
      'https://images.unsplash.com/photo-1462331940025-496dfbfc7564?q=80&w=1200&auto=format&fit=crop&ixlib=rb-4.0.3&s=3f5f6a3a6a3f4b3c2d1e6f7a8b9c0d1e',
    src: 'https://www.w3schools.com/html/mov_bbb.mp4'
  },
  {
    id: 4,
    title: 'Street Rhythm',
    author: 'Urban Beats',
    duration: '01:58',
    description: '街头节奏与快速剪辑。',
    thumb:
      'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?q=80&w=1200&auto=format&fit=crop&ixlib=rb-4.0.3&s=4e6f1a2b3c4d5e6f7a8b9c0d1e2f3a4b',
    src: 'https://www.w3schools.com/html/mov_bbb.mp4'
  },
  {
    id: 5,
    title: 'Quiet Forest',
    author: 'Calm Studio',
    duration: '06:05',
    description: '在森林中漫步，放空心灵。',
    thumb:
      'https://images.unsplash.com/photo-1501785888041-af3ef285b470?q=80&w=1200&auto=format&fit=crop&ixlib=rb-4.0.3&s=6b7a8c9d0e1f2a3b4c5d6e7f8a9b0c1d',
    src: 'https://www.w3schools.com/html/mov_bbb.mp4'
  },
  {
    id: 6,
    title: 'Digital Dreams',
    author: 'Pixel Lab',
    duration: '05:45',
    description: '抽象数字艺术短片。',
    thumb:
      'https://images.unsplash.com/photo-1499084732479-de2c02d45fc4?q=80&w=1200&auto=format&fit=crop&ixlib=rb-4.0.3&s=9a8b7c6d5e4f3a2b1c0d9e8f7a6b5c4d',
    src: 'https://www.w3schools.com/html/mov_bbb.mp4'
  }
]

const filtered = computed(() => {
  const q = query.value.trim().toLowerCase()
  if (!q) return sampleVideos
  return sampleVideos.filter(
    (v) =>
      v.title.toLowerCase().includes(q) ||
      v.author.toLowerCase().includes(q) ||
      v.description.toLowerCase().includes(q)
  )
})

function open(video) {
  player.value = video
  // small delay to ensure video element exists when auto-playing
  setTimeout(() => {
    if (videoEl.value) {
      try {
        videoEl.value.play()
      } catch (e) {
        console.error(e)
      }
    }
  }, 50)
}

function close() {
  if (videoEl.value) {
    try {
      videoEl.value.pause()
    } catch (e) {
      console.error(e)
    }
  }
  player.value = null
}

onMounted(() => {
  // subtle background pulse using CSS variables could be set here if needed
})
</script>

<style scoped>
/* Page background */
.page {
  min-height: 100vh;
  background: radial-gradient(
      1200px 600px at 10% 10%,
      rgba(58, 123, 213, 0.12),
      transparent
    ),
    radial-gradient(
      1000px 400px at 90% 90%,
      rgba(131, 58, 180, 0.12),
      transparent
    ),
    linear-gradient(180deg, #0f0f17 0%, #060610 100%);
  color: #e6eef8;
  padding: 28px;
  box-sizing: border-box;
}

/* Top bar */
.topbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 22px;
}

.title {
  margin: 0;
  font-size: 22px;
  letter-spacing: 0.6px;
  text-shadow: 0 2px 14px rgba(58, 123, 213, 0.18);
}

.controls {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.06);
  color: #e6eef8;
  padding: 8px 12px;
  border-radius: 10px;
  outline: none;
  width: 220px;
  transition: box-shadow 0.2s, transform 0.15s;
}

.search:focus {
  box-shadow: 0 6px 30px rgba(58, 123, 213, 0.18);
  transform: translateY(-1px);
}

/* Grid */
.stage {
  padding-top: 6px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 18px;
}

/* Card */
.card {
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.02),
    rgba(255, 255, 255, 0.01)
  );
  border: 1px solid rgba(255, 255, 255, 0.04);
  border-radius: 14px;
  overflow: hidden;
  cursor: pointer;
  transform: translateZ(0);
  transition: transform 0.25s cubic-bezier(0.2, 0.9, 0.2, 1), box-shadow 0.25s;
  box-shadow: 0 6px 30px rgba(2, 6, 23, 0.5);
  position: relative;
}

.card:hover {
  transform: translateY(-10px) scale(1.02);
  box-shadow: 0 18px 60px rgba(58, 123, 213, 0.14);
}

/* Thumb */
.thumbWrap {
  position: relative;
  height: 150px;
  overflow: hidden;
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.02),
    rgba(0, 0, 0, 0.08)
  );
}

.thumb {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.6s cubic-bezier(0.2, 0.9, 0.2, 1);
}

.card:hover .thumb {
  transform: scale(1.06) rotate(0.6deg);
}

/* preview video overlay */
.preview {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  mix-blend-mode: normal;
  background: transparent;
}

/* badge and time */
.badge {
  position: absolute;
  left: 12px;
  top: 12px;
  width: 36px;
  height: 36px;
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.06),
    rgba(255, 255, 255, 0.02)
  );
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  filter: drop-shadow(0 6px 18px rgba(58, 123, 213, 0.18));
  transition: transform 0.18s;
}

.badge svg {
  width: 18px;
  height: 18px;
  opacity: 0.95;
  color: #a6d1ff;
}

.time {
  position: absolute;
  right: 12px;
  top: 12px;
  background: rgba(2, 6, 23, 0.6);
  padding: 6px 8px;
  border-radius: 8px;
  font-size: 12px;
  color: #dbeeff;
  border: 1px solid rgba(255, 255, 255, 0.04);
}

/* meta */
.meta {
  padding: 12px 14px 16px;
}

.titleRow {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.vtitle {
  margin: 0;
  font-size: 15px;
  line-height: 1.1;
  color: #fff;
  text-shadow: 0 4px 22px rgba(58, 123, 213, 0.08);
}

.author {
  font-size: 12px;
  color: #9fbffc;
}

.desc {
  margin: 8px 0 0;
  font-size: 13px;
  color: #cfe6ff;
  opacity: 0.85;
}

/* modal */
.modal {
  position: fixed;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(
    180deg,
    rgba(3, 5, 12, 0.6),
    rgba(3, 5, 12, 0.85)
  );
  z-index: 1200;
}

.playerCard {
  width: min(1100px, 94%);
  max-height: 92vh;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0.02),
    rgba(0, 0, 0, 0.06)
  );
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 30px 80px rgba(2, 6, 23, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.04);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.playerHeader {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.closeBtn {
  background: rgba(255, 255, 255, 0.03);
  border: none;
  color: #fff;
  font-size: 22px;
  width: 38px;
  height: 38px;
  border-radius: 10px;
  cursor: pointer;
}

.bigVideo {
  width: 100%;
  border-radius: 8px;
  background: black;
  max-height: 62vh;
  outline: none;
}

.playerInfo {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #a6d1ff;
  font-size: 13px;
}

.tags {
  display: flex;
  gap: 8px;
}

.tag {
  background: rgba(58, 123, 213, 0.12);
  color: #a6d1ff;
  padding: 6px 8px;
  border-radius: 8px;
  font-size: 12px;
}

/* empty state */
.empty {
  text-align: center;
  color: rgba(255, 255, 255, 0.6);
  padding: 80px 12px;
  grid-column: 1/-1;
}

/* transition */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.18s;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

/* responsive tweaks */
@media (max-width: 640px) {
  .thumbWrap {
    height: 140px;
  }

  .search {
    width: 140px;
  }

  .title {
    font-size: 18px;
  }
}
</style>
