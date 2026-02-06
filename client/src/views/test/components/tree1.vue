<template>
  <div class="tree-drag">
    <el-tree ref="tree1" :data="treeData1" class="tree" node-key="id" draggable default-expand-all :allow-drop="returnFalse" @node-drag-start="handleDragstart" @node-drag-end="handleDragend"></el-tree>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      treeData1: [{
        id: 1,
        label: '一级 1',
        children: [{
          id: 4,
          label: '二级 1-1',
          children: [{ id: 9, label: '三级 1-1-1' }]
        }]
      }]
    }
  },
  methods: {
    handleDragstart (node, event) {
      this.$refs.tree2.$emit('tree-node-drag-start', event, { node: node })
    },
    returnFalse () {
      return false
    }
  }
}
</script>

<style scoped>
.tree {
  display: inline-block;
  vertical-align: top;
  width: 30%;
  height: 400px;
  border: 1px solid #999;
}
</style>
