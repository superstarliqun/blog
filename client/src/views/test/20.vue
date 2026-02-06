<!-- <template>
    <div>
        <div v-for="(item, index) in list" :key="index" class="abc">
            <span>{{ item.launchId }}</span>
            <span>{{ item.type }}</span>
            <span>{{ item.flag }}</span>

            <span>{{ item.involved }}
                <el-radio-group v-model="item.involved">
                    <el-radio-button :label="true">TRUE</el-radio-button>
                    <el-radio-button :label="false">FALSE</el-radio-button>
                </el-radio-group>
            </span>
            <span>{{ item.description }}</span>

        </div>
        <el-button @click="requestData">测试</el-button>
    </div>
</template>

<script>
export default {

    data() {
        return {
            list: [
                { 'launchId': 3000421, 'type': '是否通过代码安全审计，无中、高以上危漏洞', 'flag': true, 'involved': false, 'description': '', 'createdTime': '2025-10-13 18:09:57', 'updatedTime': '2025-10-13 18:09:57', 'id': 105001181 },
                { 'launchId': 3000421, 'type': '是否通过漏洞扫描', 'flag': false, 'involved': false, 'description': '测试保存提交检查点2', 'createdTime': '2025-10-13 18:09:57', 'updatedTime': '2025-10-13 18:09:57', 'id': 105001182 },
                { 'launchId': 3000421, 'type': '是否通过安全渗透测试，无中、高以上危漏洞', 'flag': false, 'involved': false, 'description': '测试保存提交检查点3', 'createdTime': '2025-10-13 18:09:57', 'updatedTime': '2025-10-13 18:09:57', 'id': 105001183 },
                { 'launchId': 3000421, 'type': '是否已设置系统备份', 'flag': false, 'involved': false, 'description': '测试保存提交检查点4', 'createdTime': '2025-10-13 18:09:57', 'updatedTime': '2025-10-13 18:09:57', 'id': 105001184 },
                { 'launchId': 3000421, 'type': '是否正确配置服务器时钟同步', 'flag': false, 'involved': false, 'description': '测试保存提交检查点5', 'createdTime': '2025-10-13 18:09:57', 'updatedTime': '2025-10-13 18:09:57', 'id': 105001185 },
                { 'launchId': 3000421, 'type': '是否新建、变更软件许可或数字证书', 'flag': false, 'involved': false, 'description': '测试保存提交检查点6', 'createdTime': '2025-10-13 18:09:57', 'updatedTime': '2025-10-13 18:09:57', 'id': 105001186 }
            ]
        }
    },
    created() {
        this.requestData()
    },
    methods: {
        requestData() {
            console.log(this.list)
            // const isCanSave = this.list.every((item) => (item.involved ? !!(item.flag && String(item.flag).trim() !== '') : !!(item.description && String(item.description).trim() !== '')))
            const isCanSave = this.list.every((item) => (item.involved ? !!item.flag : !!item.description))
            console.log('最终结果：', isCanSave)
            const item = this.list[0]
            console.log('lq1', item.involved)
            console.log('lq2', item.flag)
            console.log('lq3', item.description)
            console.log('lq4', item.involved ? item.flag : item.description)
            console.log('lq5', item.involved ? '1' : '2')
        },
        test() {
            const getRelatedBudget = async (key: string, _value: SelectValue, _type: string, _budgetId?: number) => {
                await api.projectApi.payment.getPayBudgetByProIdAndCompany(Number(route.params.id), { companyName: String(_value) }, (data) => {
                    dataSource.value?.forEach((item2) => {
                    const item2 = dataSource.value?[_currentindex]
                    if (item2.companyName === _value || item2.companyName === '') {
                        item2.budgetOptions = data.map((info) => {
                            return {
                                value: info.id,
                                label: info.name
                            }
                        })
                    } else {
                        item2.budgetOptions = []
                    }
                    if (_type === 'change') {
                        if (item2.budgetOptions?.length === 1) {
                            editableData[key]['budgetId'] = item2.budgetOptions[0]?.value
                        } else {
                            item2.budgetOptions = []
                            editableData[key]['budgetId'] = undefined
                        }
                    } else {
                        editableData[key]['budgetId'] = _budgetId
                    }
                })
            }
        }
    }

}
</script>

<style>
.abc {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
}
</style> -->
