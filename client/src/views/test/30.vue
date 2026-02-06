<!-- <template>
  <div>
    <div class="demo" />
    <div ref="editor" />
  </div>

</template>

<script>
import { defaultValueCtx, Editor, rootCtx } from '@milkdown/kit/core'
import { commonmark } from '@milkdown/kit/preset/commonmark'
import { nord } from '@milkdown/theme-nord'
import { SlashProvider, slashFactory } from '@milkdown/plugin-slash'
import { tooltipFactory } from '@milkdown/plugin-tooltip'
import { TooltipProvider } from '@milkdown/kit/plugin/tooltip'

export default {
  name: 'Editor',
  data() {
    return {
      msg: ''
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initMilkdown()
    })
  },
  methods: {
    async initMilkdown() {
      try {
        const tooltip = tooltipFactory('my-tooltip')
        const menu = document.createElement('div')
        menu.className = 'slash-menu'
        menu.style.cssText = `position:absolute;padding:4px 0;background:white;border:1px solid #eee;box-shadow:0 2px 8px rgba(0,0,0,.15);border-radius:6px;font-size:14px;`
        menu.innerHTML = `<ul style="margin:0;padding:0;list-style:none">
        <li data-cmd="h1" style="padding:4px 12px;cursor:pointer">Heading 1</li>
        <li data-cmd="bullet" style="padding:4px 12px;cursor:pointer">Bullet List</li>
        </ul>`
        menu.addEventListener('click', (e) => {
          const target = e.target
          const cmd = target.dataset.cmd
          alert(`Run command: ${cmd}`)
        })
        const provider = new SlashProvider({
          content: menu,
          shouldShow(view) {
            const content = provider.getContent(view) ?? ''
            const ok = content === '/'
            if (!ok) provider.hide()
            return ok
          },
          offset: 0
        })
        const slash = slashFactory('demo')
        Editor.make()
          .config((ctx) => {
            ctx.set(rootCtx, this.$refs.editor)
            ctx.set(defaultValueCtx, this.msg)
            ctx.set(slash.key, {
              view: () => ({
                update: provider.update,
                destroy: provider.destroy
              })
            })
            ctx.set(tooltip.key, {
              view: this.tooltipPluginView()
            })
          })
          .use(commonmark)
          .use(slash)
          .use(tooltip)
          .use(nord)
          .create()
      } catch (e) {
        console.error('Milkdown Init Error:', e)
      }
    },
    tooltipPluginView(view) {
      const content = document.createElement('my-tooltip')
      const provider = new TooltipProvider({
        content: content
      })
      return {
        update: (updatedView, prevState) => {
          provider.update(updatedView, prevState)
        },
        destroy: () => {
          provider.destroy()
          content.remove()
        }
      }
    }
  }
}
</script>
<style>
/* 必须引入主题，否则斜杠菜单可能不可见 */
</style> -->
