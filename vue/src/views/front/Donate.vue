<template>
  <div style="min-height: calc(100vh - 60px); margin: 10px 0">
    <el-card style="font-size: 20px; line-height: 40px; color: #666; padding: 20px 100px">
      <div>详情请联系负责人: 小澳</div>
      <div>联系电话: 15586416729</div>
<!--      <el-button @click="handlePayment">支付宝捐赠</el-button>-->
<!--      <div style="margin-top: 20px">-->
<!--        <el-image style="width: 200px" src="http://www.yunvhs.cn/service/Uploads/image/News/201709/59c085635686a.png"-->
<!--                  :preview-src-list="['http://www.yunvhs.cn/service/Uploads/image/News/201709/59c085635686a.png']"></el-image>-->
<!--      </div>-->

<!--      <div>-->
<!--        <div v-html="alipayForm" ref="alipayContainer"></div>-->
<!--      </div>-->
      <el-button type="success" @click="dialogVisible = true">我要捐赠</el-button>


      <el-dialog
          title="捐赠页面"
          :visible.sync="dialogVisible"
          width="30%"
          :before-close="handleClose">

        <el-form  ref="form" :model="form" label-width="80px">
        <el-input type="number" placeholder="请输入内容" v-model="form.money" class="input-with-select">
          <template slot="prepend">您要捐赠多少元</template>
          <el-button slot="append" icon="el-icon-check" @click="success"></el-button>
        </el-input>
        <el-date-picker type="date" placeholder="选择日期" v-model="form.time" style="width: 100%;"></el-date-picker>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="dialogVisible = false">关闭</el-button>
  </span>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      alipayForm: '',
      dialogVisible: false,
      form:{
        money:0,
        name:localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')).nickname:'',
        time:new Date().toISOString().split('T')[0] // 直接获取今天的ISO格式日期
      },

    }
  },

  methods: {

    success() {
      this.form.money = parseInt(this.form.money.toString(), 10) || 0;
      this.request.post('/donate/money',{
      ...this.form
      }).then(res=>{
        if(res.code==200){
          this.$message({
            message: `恭喜你,成功捐赠${this.form.money}元`,
            type: 'success'
          });
        }else{
          this.$message({
            message: `捐款失败`,
            type: 'error'
          });
        }
      })

    },
    handleClose(done) {
      this.$confirm('确认关闭')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
    async handlePayment() {
      try {
        const response = await this.request.get('alipay/pay', {
          params: { traceNo: '8' ,totalAmount:100,subject:'哇哈哈',}
        });

        // 将支付宝返回的HTML表单存入data
        this.alipayForm = response.data;

        // 在下一个tick确保DOM已更新
        this.$nextTick(() => {
          // 自动提交表单
          this.$refs.alipayContainer.querySelector('form').submit();
        });

      } catch (error) {
        console.error('支付失败:', error);
        this.$message.error('支付请求失败');
      }
    }
  }
}
</script>


<style scoped>

</style>