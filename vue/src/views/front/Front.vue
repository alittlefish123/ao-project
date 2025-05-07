<template>
  <div style="background: #F0F8FF">
<!--    头部-->
    <div style="display: flex; background-color: #fff; height: 60px; line-height: 60px; border-bottom: 1px solid #eee">
      <div style="width: 300px; display: flex; padding-left: 30px">
        <div style="width: 60px">
          <img src="../../assets/宠物招牌.png" alt="" style="width: 40px; position: relative; top: 10px; right: 0">
        </div>
        <div style="flex: 1; font-size: 20px; color: #1E90FF; font-weight: bold;">流浪动物救助中心系统</div>
      </div>
      <div style="flex: 1">

        <el-menu :default-active="'1'" class="el-menu-demo" mode="horizontal" router>
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/adopt">宠物领养</el-menu-item>
          <el-menu-item index="/front/salvation">流浪宠物救助</el-menu-item>
          <el-menu-item index="/front/feed">流浪动物喂养点</el-menu-item>
          <el-menu-item index="/front/lost">丢失宠物查看</el-menu-item>
          <el-menu-item index="/front/rescue">流浪宠物救助站</el-menu-item>
          <el-menu-item index="/front/article">宠物论坛</el-menu-item>
          <el-menu-item index="/front/donate">流浪动物捐赠</el-menu-item>
<!--          <el-menu-item index="/front/articleKp">科普文章</el-menu-item>-->
<!--          <el-menu-item index="/front/activity">热门活动</el-menu-item>-->

        </el-menu>
      </div>
      <div style="width: 160px">
        <div v-if="!user.username" style="text-align: right; padding-right: 30px">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
            <div style="display: inline-block">
              <img :src="user.avatarUrl" alt=""
                   style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
              <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
              <el-dropdown-item style="font-size: 14px; padding: 5px 0" v-if="user.role === 'ROLE_ADMIN'">
                <router-link to="/">后台管理</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0" v-if="user.role === 'ROLE_VOLUNTEER'">
                <router-link to="/tasks">任务清单管理</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0" >
                <el-button type="primary" v-if="user.role==='ROLE_USER'" @click="apply">申请成为志愿者</el-button>
                <el-button type="info" v-if="user.role==='ROLE_TEMP'" @click="update">申请待批准请稍后</el-button>
                <el-button type="success" v-if="user.role==='ROLE_VOLUNTEER'" @click="update">我是光荣的志愿者</el-button>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/password">修改密码</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/front/person">个人信息</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <span style="text-decoration: none" @click="logout">退出</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <div style="width: 800px; margin: 0 auto">
      <router-view />
    </div>
  </div>
</template>

<script>
import {createLogger} from "vuex";

export default {
  name: "Front",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {

  },
  methods: {
    update(){
      let userId=JSON.parse(localStorage.getItem('user')).id
      this.request.get("/role/update",{
        params:{userId:userId}
      }).then(res=>{
        if(res.code==200){
          this.user.role=res.data.role
          console.log(this.user.role)
          localStorage.setItem('user',JSON.stringify(this.user))
        }
      })
    },
    apply(){
      this.request.get("/role/apply",{
        params:{userId:this.user.id}
      }).then(res=>{
        if(res.code==200 && this.user.role==="ROLE_USER"){
          this.user.role='ROLE_TEMP'
          localStorage.setItem('user',JSON.stringify(this.user))
          console.log(localStorage.getItem('user'))
        }
      })
      this.update()
    },
    logout() {
      this.$store.commit("logout")
      this.$message.success("退出成功")
    }
  }
}
</script>

<style>
.item{
  display: inline-block;
  width: 100px;

  text-align: center;
  cursor: pointer
}
.item a {
  color: 	#1E90FF;
}
.item:hover{
  background-color: 	LightPink;
}
</style>
