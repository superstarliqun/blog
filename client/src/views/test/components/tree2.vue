<template>
  <div class="tree-drag">
    <el-tree ref="tree2" :data="treeData2" class="tree" node-key="id" draggable default-expand-all :allow-drop="returnTrue"></el-tree>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      treeData2: [{
        id: 2,
        label: '一级 1',
        children: [{
          id: 5,
          label: '二级 1-1',
          children: [{ id: 8, label: '三级 1-1-1' }]
        }]
      }]
    }
  },
  methods: {
    returnTrue () {
      return true
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
