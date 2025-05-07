<template>
  <div>
    <div style="margin: 10px 0" v-if="this.uid==1">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div v-else>
      <el-select v-model="state" placeholder="是否完成">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button type="primary" @click="load">点击搜索</el-button>
    </div>
    <div style="margin: 10px 0">

      <!-- Form -->
      <el-button type="primary" @click="dialogFormVisible = true" v-if="this.uid==1">创建任务 <i class="el-icon-edit-outline"></i></el-button>

      <el-dialog title="创建任务" :visible.sync="dialogFormVisible" >
        <el-form :model="form">
          <el-form-item label="任务名称" :label-width="formLabelWidth">

            <el-select
                v-model="form.name"
                placeholder="请选择任务名"
                filterable
                loading-text="加载中..."
                :loading="taskNameLoading"
                @focus="loadTaskNames"
            >
              <el-option
                  v-for="item in taskNameOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="截止日期" :label-width="formLabelWidth">
            <el-date-picker
                v-model="form.date"
                type="date"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd">
              placeholder="截止日期">
            </el-date-picker>
          </el-form-item>


          <el-form-item label="任务描述" :label-width="formLabelWidth">
            <el-input type="textarea" v-model="form.description" autocomplete="off"></el-input>
          </el-form-item>


          <el-form-item label="志愿者" :label-width="formLabelWidth">
            <el-select
                v-model="form.uid"
                placeholder="请选择志愿者"
                filterable
                loading-text="加载中..."
                :loading="volunteerLoading"
                @focus="loadVolunteers"
            >
            <el-option
                v-for="item in volunteerOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            ></el-option>
            </el-select>
          </el-form-item>

<!--          <el-form-item label="活动区域" :label-width="formLabelWidth">-->
<!--            <el-select v-model="form.region" placeholder="请选择活动区域">-->
<!--              <el-option label="区域一" value="shanghai"></el-option>-->
<!--              <el-option label="区域二" value="beijing"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="addTask">确 定</el-button>
        </div>
      </el-dialog>

      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"

      >
        <el-button v-if="this.uid==1" type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>

    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="任务名">
        <template #default="scope">
          {{ taskNameMap[scope.row.nameId]}}
        </template>
      </el-table-column>
      <el-table-column prop="description" label="个人描述"></el-table-column>
      <el-table-column label="任务状态">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.state"
                     :active-value='1' :inactive-value="0"
                     active-color="#13ce66" inactive-color="#ccc"
                     @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="date" label="截止时间"></el-table-column>

        <el-table-column label="志愿者名">
          <template #default="scope">
            {{ nameMap[scope.row.uid] || "加载中..." }}
          </template>
        </el-table-column>
      <el-table-column prop="phone" label="手机号码"></el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
export default {
  name: "Task",
  data() {
    return {
      // 普通志愿者  下拉框选项
      options: [{
        value: '0',
        label: '未完成'
      }, {
        value: '1',
        label: '已完成'
      }],



      //id转name
      nameMap: {}, // 缓存 uid -> name 的映射
      taskNameMap:{},  //uid-> taskName
      loadingMap: {}, // 记录哪些 uid 正在加载

      // 新建任务
      dialogFormVisible: false,
      form: {
        name: '',
        description: '',
        state: '',
        date: '',
        uid: "",
        phone:"",
        action: '删除',
      },
      volunteerOptions:[],
      taskNameOptions:[],
      volunteerLoading:false,
      taskNameLoading:false,
      formLabelWidth: '120px',
      tableData: [],
      name: JSON.parse(localStorage.getItem('user')).id,
      uid: JSON.parse(localStorage.getItem('user')).id,
      state:'',  // 任务状态
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.load()
  },
  computed: {
    volunteerPhone() {
      if (!this.form.uid) return "";
      // 根据选中的 uid 查找对应的 phone
      const volunteer = this.volunteerOptions.find(v => v.id === this.form.uid);
      console.log(volunteer)
      return volunteer ? volunteer.phone : "";
    }
  },
  watch: {
    // 当 uid 变化时更新 phone
    'form.uid'(newVal) {
      this.form.phone = this.volunteerPhone;

    }
  },
  methods: {

    async loadAllUserNames() {
      const uids = this.tableData.map(item => item.uid);
      const response = await this.request.post("/task/user/batch", uids );
      this.nameMap = response.data.reduce((map, user) => {
        map[user.id] = user.nickname;
        return map;
      }, {});
    },
    async loadAllTaskNames() {
      const tids = this.tableData.map(item => item.nameId);
      const response = await this.request.post("/task/taskNames", tids );
      this.taskNameMap = response.data.reduce((map, task) => {
        map[task.id] = task.name;
        return map;
      }, {});
      console.log(this.taskNameMap)
    },

    //增加任务
    async addTask(){
      this.request.post('/task/add', {...this.form,nameId:this.form.name}
      ).then(success=>{

        this.$message.success("添加任务成功")
      },err=>{
        this.$message.error("添加任务失败")
      })
      this.dialogFormVisible = false
    },
    // 加载志愿者列表
    async loadVolunteers() {
      // 如果已经加载过，不再重复请求
      if (this.volunteerOptions.length > 0) return;
      this.volunteerLoading = true;
      try {
        const response = await this.request.post('/task/user/all');
        this.volunteerOptions = response.data.map(item => ({
          id: item.id,
          name: item.nickname,
          phone:item.phone
        }));
        console.log(this.volunteerOptions)
      } catch (error) {
        this.$message.error('加载志愿者列表失败');
        console.error(error);
      } finally {
        this.volunteerLoading = false;
      }
    },

    async loadTaskNames() {
      // 如果已经加载过，不再重复请求
      if (this.taskNameOptions.length > 0) return;

      this.taskNameLoading = true;
      try {
        const response = await this.request.post('/task/names');
        this.taskNameOptions = response.data.map(item => ({
          id: item.id,
          name: item.name
        }));
      } catch (error) {
        this.$message.error('加载任务列表失败');
        console.error(error);
      } finally {
        this.taskNameLoading = false;
      }
    },

    async load() {
      this.request.get("/task/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          state:this.state,  // 任务状态

        }
      }).then(async res => {
        this.tableData=res.data.records.map((item)=>({
          ...item,
          state:Number(item.state)
        }))
        this.total = res.data.total
        await this.loadAllTaskNames();
        await this.loadAllUserNames();

      })
    },
    changeEnable(row) {
      this.request.post("/task/update", row).then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
        }
      })
    },
    del(id) {
      this.request.delete("/task/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/task/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleFileUploadSuccess(res) {
      this.$message.success("上传成功")
      this.load()
    },
    download(url) {
      window.open(url)
    },
    preview(url) {
      window.open('https://file.keking.cn/onlinePreview?url=' + encodeURIComponent(window.btoa((url))))
    },
  }
}
</script>

<style scoped>

</style>
