<template>
  <div style="color: #666;font-size: 14px;">
    <div style="padding-bottom: 20px">
      <b>您好！{{ user.nickname }}</b>
    </div>

    <div style="margin-bottom: 60px" >
      <el-card>
        欢迎使用本系统
        <el-divider />
        以爱之名，宠你一生！
      </el-card>
    </div>

    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px; height: 400px"></div>
      </el-col>

      <el-col :span="12">
        <div id="pie" style="width: 500px; height: 400px"></div>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <div id="rescueCount" style="width: 500px; height: 400px"></div>
      </el-col>

      <el-col :span="12">
        <div id="rescueCountPie" style="width: 500px; height: 400px"></div>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <div id="donate" style="width: 500px; height: 400px"></div>
      </el-col>

      <el-col :span="12">
        <div id="donatePie" style="width: 500px; height: 400px"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import * as echarts from 'echarts';

export default {
  name: "Home",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  mounted() {  // 页面元素渲染后再触发

    var option = {
      title: {
        text: '各季度系统注册人数统计',
        subtext: '趋势图',
        left: 'center'
      },tooltip: {
        trigger: 'axis', // 坐标轴触发
        axisPointer: {
          type: 'shadow' // 使用阴影指示器
        },
        formatter: '{b}: {c}' // 自定义提示框内容，{b} 表示类目名称，{c} 表示数值[^5^]
      },

      xAxis: {
        type: 'category',
        data: []
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'line'
        },
        {
          data: [],
          type: 'bar'
        }
      ]
    };
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    this.request.get("/echarts/members").then(res => {
      let data=res.data
      option.xAxis.data=data.map(item=>item.time1)

      // 填空
      // option.xAxis.data = res.data.x
      option.series[0].data = data.map(item=>item.count1)
      option.series[1].data = option.series[0].data
      // 数据准备完毕之后再set
      myChart.setOption(option);
    })

    //统计个年份的捐款的柱状图
    var donateOption = {
      title: {
        text: '各季度系统募捐统计',
        subtext: '趋势图',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis', // 坐标轴触发
        axisPointer: {
          type: 'shadow' // 使用阴影指示器
        },
        formatter: '{b}: {c}' // 自定义提示框内容，{b} 表示类目名称，{c} 表示数值[^5^]
      },

      xAxis: {
        type: 'category',
        data: []
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'line'
        },
        {
          data: [],
          type: 'bar'
        }
      ]
    };
    var donateDom = document.getElementById('donate');
    var donateChart = echarts.init(donateDom);
    this.request.get("/echarts/members3").then(res => {
      if(res.code==200&&res){
        donateOption.xAxis.data=res.data.map(item=>item.time1)
        donateOption.series[0].data=res.data.map(i=>i.countMoney)
        donateOption.series[1].data=res.data.map(i=>i.countMoney)
      }
      // 数据准备完毕之后再set
      donateChart.setOption(donateOption);
    })


    //各季度注册人数的饼图
    var pieOption = {
      title: {
        text: '各季度系统注册人数统计',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,    //文字的字体大小
                color: "#fff"
              },
              formatter:'{d}%'
            }
          },
          data: [],  // 填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);

    this.request.get("/echarts/members").then(res => {
      pieOption.series[0].data = res.data.map(item=>{return {name:item.time1,value:item.count1}})
      pieChart.setOption(pieOption)
    })

    // 捐赠同级饼图
    var donatePieOption = {
      title: {
        text: '各季度系统捐赠统计',
        subtext: '比例图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,    //文字的字体大小
                color: "#fff"
              },
              formatter:'{d}%'
            }
          },
          data: [],  // 填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };

    var donatePie = document.getElementById('donatePie');
    var donatePieChart = echarts.init(donatePie);

    this.request.get("/echarts/members3").then(res => {
      if(res.code==200){
        let data=res.data
        donatePieOption.series[0].data =  data.map((item)=>{
          return {name:item.time1,value:item.countMoney}
        })

      }

      donatePieChart.setOption(donatePieOption)
    })


    var rescueAnimalCountOption = {
      title: {
        text: '各季度救助动物数量统计',
        subtext: '比例图2',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '60%',
          label:{            //饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', //标签的位置
              textStyle : {
                fontWeight : 300 ,
                fontSize : 14,    //文字的字体大小
                color: "#fff"
              },
              formatter:'{d}%'
            }
          },
          data: [],  // 填空
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };

    var pieDom2 = document.getElementById('rescueCountPie');
    var pieChart2 = echarts.init(pieDom2);

    this.request.get("/echarts/members2").then(res => {
      const data=res.data;


      rescueAnimalCountOption.series[0].data =  data.map((item)=>{
        return {name:item.time1,value:item.count1}
      })
      // {name: "第一季度", value: res.data[0]},
      // {name: "第二季度", value: res.data[1]},
      // {name: "第三季度", value: res.data[2]},
      // {name: "第四季度", value: res.data[3]},


      pieChart2.setOption(rescueAnimalCountOption)
    })
    // 救助动物的线型图
    var rescueLineOption = {
      title: {
        text: '各季度系统救助动物统计',
        subtext: '趋势图',
        left: 'center'
      },tooltip: {
        trigger: 'axis', // 坐标轴触发
        axisPointer: {
          type: 'shadow' // 使用阴影指示器
        },
        formatter: '{b}: {c}' // 自定义提示框内容，{b} 表示类目名称，{c} 表示数值[^5^]
      },

      xAxis: {
        type: 'category',
        data: []
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'line'
        },
        {
          data: [],
          type: 'bar'
        }
      ]
    };
    var rescueLine = document.getElementById('rescueCount');
    var rescueChart = echarts.init(rescueLine);
    this.request.get("/echarts/members2").then(res => {
      if(res.code==200&&res){
        rescueLineOption.xAxis.data=res.data.map(item=>item.time1)
        rescueLineOption.series[0].data=res.data.map(i=>i.count1)
        rescueLineOption.series[1].data=res.data.map(i=>i.count1)
      }
      // 数据准备完毕之后再set
      rescueChart.setOption(rescueLineOption);
    })


  }
}
</script>
