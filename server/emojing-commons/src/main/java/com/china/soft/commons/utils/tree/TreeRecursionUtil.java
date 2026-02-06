package com.china.soft.commons.utils.tree;

import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 树形结构递归处理工具类（原生Java实现，高性能）
 * 大厂开发规范：工具类采用final修饰，私有化构造方法，避免被实例化和继承
 */
public final class TreeRecursionUtil {

    // 私有化构造方法，禁止外部实例化
    private TreeRecursionUtil() {
        throw new UnsupportedOperationException("工具类不允许实例化");
    }

    /**
     * 构建树形结构（核心递归方法）
     * @param nodeList 原始扁平节点列表（查询出来的List对象）
     * @param rootParentId 根节点父ID（通常为0或null）
     * @param <T> 泛型约束，必须继承TreeEntity
     * @return 树形结构列表（根节点集合，包含所有子节点递归嵌套）
     */
    public static <T extends TreeEntity> List<T> buildTree(List<T> nodeList, Long rootParentId) {
        // 1. 判空处理（大厂规范：前置参数校验，避免空指针）
        if (CollectionUtils.isEmpty(nodeList)) {
            return new ArrayList<>();
        }

        // 2. 构建节点ID -> 节点对象的Map（O(1)查询效率，解决重复遍历List的性能问题）
        // 性能优化：初始容量设置为nodeList.size()，避免HashMap扩容的性能损耗
        Map<Long, T> nodeMap = new HashMap<>(nodeList.size());
        for (T node : nodeList) {
            nodeMap.put(node.getId(), node);
        }

        // 3. 递归构建树形结构（先筛选根节点，再递归填充子节点）
        List<T> rootNodeList = new ArrayList<>();
        for (T node : nodeList) {
            Long parentId = node.getParentId();
            // 判断是否为根节点
            if (rootParentId.equals(parentId)) {
                // 根节点定义为第 1 层
                node.setLevel(1);
                rootNodeList.add(node);
                // 递归填充当前根节点的所有子节点
                fillChildren(node, nodeMap, 1);
            }
        }

        return rootNodeList;
    }

    // ==================== 新增方法：支持父节点ID列表（核心需求） ====================
    /**
     * 构建树形结构（支持批量父节点ID）
     * @param nodeList 原始扁平节点列表
     * @param parentIdList 父节点ID列表（指定需要构建树形的多个父节点）
     * @param <T> 泛型约束，必须继承TreeEntity
     * @return 多个父节点对应的树形结构列表（每个父节点作为根，包含其所有子节点）
     */
    public static <T extends TreeEntity> List<T> buildTreeByParentIdList(List<T> nodeList, List<Long> parentIdList) {
        // 1. 前置参数校验（大厂规范：避免空指针和无效参数）
        if (CollectionUtils.isEmpty(nodeList) || CollectionUtils.isEmpty(parentIdList)) {
            return new ArrayList<>();
        }

        // 2. 构建节点ID映射表（性能核心：O(1)查询，避免重复遍历List）
        Map<Long, T> nodeMap = new HashMap<>(nodeList.size());
        for (T node : nodeList) {
            nodeMap.put(node.getId(), node);
        }

        // 3. 筛选指定父节点列表，并递归填充各自的子节点
        List<T> parentNodeTreeList = new ArrayList<>();
        for (T node : nodeList) {
            Long nodeParentId = node.getParentId();
            // 判断当前节点的父ID是否在指定的父节点ID列表中
            if (parentIdList.contains(nodeParentId)) {
                parentNodeTreeList.add(node);
                // 递归填充当前节点的所有子节点（与原有逻辑一致，保证性能）
                node.setLevel(1);
                fillChildren(node, nodeMap, 1);
            }
        }

        return parentNodeTreeList;
    }

    /**
     * 递归填充子节点（私有辅助方法，对外隐藏实现细节）
     * @param parentNode 父节点
     * @param nodeMap 节点ID映射表
     * @param <T> 泛型约束
     */
    private static <T extends TreeEntity> void fillChildren(T parentNode, Map<Long, T> nodeMap, int currentLevel) {
        int nextLevel = currentLevel + 1;

        // 1. 筛选子节点
        List<T> childrenList = nodeMap.values().stream()
                .filter(node -> parentNode.getId().equals(node.getParentId()))
                .peek(node -> node.setLevel(nextLevel))
                .collect(Collectors.toList());

        // 2. 排序逻辑：时间越大越靠前（降序）
        // 如果需要升序，改为 .comparing(TreeEntity::getCreateTime)
        if (!childrenList.isEmpty()) {
            childrenList.sort(Comparator.comparing(T::getCreateTime).reversed());
        }

        // 3. 递归填充子节点的子节点
        for (T child : childrenList) {
            fillChildren(child, nodeMap, nextLevel);
        }

        // 4. 设置子节点
        parentNode.setChildren(childrenList);
    }
}