<template>
  <div class="login-page">
    <h1 class="title">SHORT-LINK-PLATFORM</h1>
    <div class="login-box">
      <!-- login -->
      <div class="logon" :class="{ hidden: !isLogin }">
        <h2>Login</h2>
        <el-form ref="loginFormRef1" :model="loginForm" label-width="50px" :rules="loginFormRule">
          <div class="form-container1">
            <el-form-item prop="phone">
              <el-input v-model="loginForm.username" maxlength="11" show-word-limit clearable>
                <template v-slot:prepend>
                <div class="prepend-container">User Name</div>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" clearable show-password
                style="margin-top: 20px">
                <template v-slot:prepend>
                <div class="prepend-container">Password</div>
                </template>
              </el-input>
            </el-form-item>
          </div>
          <div class="btn-gourp">
            <div>
              <el-checkbox class="remeber-password" v-model="checked"
                style="color: #a0a0a0; margin: 0 0 0px 0">Remember Password</el-checkbox>
            </div>
            <div>
              <el-button :loading="loading" @keyup.enter="login" type="primary" plain
                @click="login(loginFormRef1)">Login</el-button>
            </div>
          </div>
        </el-form>
      </div>
      <!-- register -->
      <div class="signup" :class="{ hidden: isLogin }">
        <h2>Sign up</h2>
        <el-form ref="loginFormRef2" :model="userSignUpForm" label-width="100px" class="form-container" width="width"
                 :rules="addFormRule">
          <el-form-item prop="username">
            <el-input v-model="userSignUpForm.username" maxlength="11" show-word-limit clearable>
              <template v-slot:prepend >
                <div class="prepend-container">User Name</div> 
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="mail">
            <el-input v-model="userSignUpForm.mail" show-word-limit clearable>
              <template v-slot:prepend> <div class="prepend-container">Email Address</div> </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="phone">
            <el-input v-model="userSignUpForm.phone" show-word-limit clearable>
              <template v-slot:prepend> <div class="prepend-container">Phone Number</div> </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input v-model="userSignUpForm.password" type="password" clearable show-password>
              <template v-slot:prepend> <div class="prepend-container">Password</div> </template>
            </el-input>
          </el-form-item>
          <div class="btn-gourp">
            <div></div>
            <div>
              <el-button :loading="loading" @keyup.enter="login" type="primary" plain style="width:auto; padding:0 10px;"
                @click="userRegistration(loginFormRef2)">Register and login</el-button>
            </div>
          </div>
        </el-form>
      </div>
      <!-- move -->
      <div class="move" ref="moveRef">
        <span style="font-size: 18px; margin-bottom: 25px; color: rgb(225, 238, 250)">{{
          !isLogin ? 'Already have an account？' : 'Don\'t have an account？'
        }}</span>
        <span style="font-size: 16px; color: rgb(225, 238, 250)">{{
          !isLogin ? 'Login now!' : 'Sign up and use！'
        }}</span>
        <el-button style="width: 100px; margin-top: 30px" @click="changeLogin">{{
          !isLogin ? 'Login' : 'Sign Up'
        }}</el-button>
      </div>
    </div>
    <div ref="vantaRef" class="vanta"></div>
  </div>
</template>

<script setup>
import { setToken, setUsername, getUsername } from '@/core/auth.js'
import { ref, reactive, onMounted, onBeforeUnmount, watch, getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import * as THREE from 'three'
import WAVES from 'vanta/src/vanta.waves'
const { proxy } = getCurrentInstance()
const API = proxy.$API
const loginFormRef1 = ref()
const loginFormRef2 = ref()
const router = useRouter()
const loginForm = reactive({
  username: '',
  password: '',
})
const userSignUpForm = reactive({
  username: '',
  password: '',
  phone: '',
  mail: ''
})

const addFormRule = reactive({
  phone: [
    { required: true, message: 'Please enter your phone number', trigger: 'blur' },
    {
      pattern: /^1[3|5|7|8|9]\d{9}$/,
      message: 'Please enter a valid phone number',
      trigger: 'blur'
    },
    { min: 11, max: 11, message: 'Phone number length must be 11', trigger: 'blur' }
  ],
  username: [{ required: true, message: 'Please enter your name', trigger: 'blur' }],
  password: [
    { required: true, message: 'Please enter the password', trigger: 'blur' },
    { min: 8, max: 15, message: 'Password length should be no less than 8', trigger: 'blur' }
  ],
  mail: [
    { required: true, message: 'Please enter your email', trigger: 'blur' },
    {
      pattern: /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/,
      message: 'Please enter a valid email address',
      trigger: 'blur'
    }
  ],
})
const loginFormRule = reactive({
  username: [{ required: true, message: 'please enter the username', trigger: 'blur' }],
  password: [
    { required: true, message: 'Please enter your password', trigger: 'blur' },
    { min: 8, max: 15, message: 'Password length must be between 8 and 15 characters', trigger: 'blur' }
  ],
})
// 注册
const userRegistration = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      try {
        // Check username existence
        const res1 = await API.user.hasUsername({username: userSignUpForm.username})
        if (res1.data.success === false) {
          ElMessage.warning('Username already exists!')
          return
        }

        //register new user but failed
        const res2 = await API.user.register(userSignUpForm)
        if (res2.data.success === false) {
          ElMessage.warning(res2.data.message)
          return
        }

        //successfully registered and login
        const res3 = await API.user.login(userSignUpForm.username, userSignUpForm.password)
        const token = res3?.data?.data?.token
        if (token) {
          setToken(token)
          setUsername(userSignUpForm.username)
          localStorage.setItem('token', token)
          localStorage.setItem('username', userSignUpForm.username)
          ElMessage.success("Successfully Registered!")
          router.push('/home')
        } else {
          ElMessage.error('Failed to get token after registration!')
        }
      } catch (error) {
        console.error("error during registration:", error)
        ElMessage.error(error.message)
      }
    } else {
      ElMessage.warning('Please fill in all required fields correctly.')
      return false
    }
  })

}

// 登录
const login = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      const res1 = await API.user.login(loginForm)
      if (res1.data.code === '0000000') {
        const token = res1?.data?.data?.token
        // 将username和token保存到cookies中和localStorage中
        if (token) {
          setToken(token)
          setUsername(loginForm.username)
          localStorage.setItem('token', token)
          localStorage.setItem('username', loginForm.username)
        }
        ElMessage.success('Login success')
        router.push('/home')
      } else if (res1.data.message === '用户已登录') {
        // 如果已经登录了，判断一下浏览器保存的登录信息是不是再次登录的信息，如果是就正常登录
        const cookiesUsername = getUsername()
        if (cookiesUsername === loginForm.username) {
          ElMessage.success('Login success')
          router.push('/home')
        } else {
          ElMessage.warning('用户已在别处登录，请勿重复登录！')
        }
      } else if (res1.data.message === '用户不存在') {
        ElMessage.error('请输入正确的账号密码!')
      }
    } else {
      ElMessage.error("Login failed, try again later.")
      return false
    }
  })
}

const loading = ref(false)
// 是否记住密码
const checked = ref(true)
const vantaRef = ref()
// 动态背景
let vantaEffect = null
onMounted(() => {
  vantaEffect = WAVES({
    el: vantaRef.value,
    THREE: THREE,
    mouseControls: true,
    touchControls: true,
    gyroControls: false,
    minHeight: 200.0,
    minWidth: 200.0,
    scale: 1.0,
    scaleMobile: 1.0
  })
})
onBeforeUnmount(() => {
  if (vantaEffect) {
    vantaEffect.destroy()
  }
})
// 展示登录还是展示注册
const isLogin = ref(true)
const moveRef = ref() // 左右移动的切换按钮模块
const changeLogin = () => {
  isLogin.value = !isLogin.value
  if (isLogin.value) {
    moveRef.value.style.transform = 'translate(0, 0)'
  } else {
    moveRef.value.style.transform = 'translate(-420px, 0)'
  }
}
</script>

<style lang="less" scoped>
.login-box {
  border: 2px solid #0984e3;
  overflow: hidden;
  display: flex;
  justify-content: space-between;
  border-radius: 20px;
  padding: 0 40px 0 40px;
  width: 700px;
  // background-color: #eee;
  position: absolute;
  z-index: 999;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-sizing: border-box;
  // border: 1px solid #eee;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  background-color: #fff;
  animation: hideIndex 0.5s;

  h2 {
    font-size: 30px;
    font-family:
      PingFangSC-Semibold,
      PingFang SC;
    font-weight: 600;
    color: #3a3f63;
    width: 100%;
    text-align: center;
    padding: 20px;
  }

  .el-form-item {
    margin-bottom: 23px;
  }

  .prepend-container {
    display: flex;
    justify-content: center;
    width: 80px; /* 根据需要调整固定宽度 */
}

  .btn-gourp {
    margin-top: 30px;
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;

    .el-button {
      width: 100px;
    }

    .remeber-password {
      left: 0;
      line-height: 0.5rem;
    }
  }

  .el-checkbox {
    width: 100%;
    text-align: center;
    margin-top: 1rem;
  }
}

/deep/ .el-form-item__content {
  margin-left: 0 !important;
}

@keyframes hideIndex {

  // <!--具体细节自己可以调整-->
  0% {
    opacity: 0;
    transform: translate(7.3125rem, -50%);
  }

  100% {
    opacity: 1;
    transform: translate(-50%, -50%);
  }
}

.login-page {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

.vanta {
  position: absolute;
  top: 0;
  right: 0;
  left: 0;
  bottom: 0;
  z-index: 0;
}

.logon {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.hidden {
  animation: hidden 1s;
  animation-fill-mode: forwards; // 保持最后的状态
}

@keyframes hidden {

  // <!--具体细节自己可以调整-->
  0% {
    opacity: 1;
  }

  70% {
    opacity: 0;
  }

  100% {
    opacity: 0;
  }
}

.move {
  position: absolute;
  right: 0;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 40%;
  transition-duration: 0.5s;
  align-items: center;
  background: #06beb6;
  /* fallback for old browsers */
  background: -webkit-linear-gradient(to right, #0984e3, #0984e3);
  /* Chrome 10-25, Safari 5.1-6 */
  background: linear-gradient(to right,
      #1a8fd5,
      #0984e3);
  /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
}

.title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  top: 15%;
  z-index: 999;
  font-size: 40px;
  color: #fff;
  font-weight: bolder;
}

:deep(.el-input__suffix-inner) {
  width: 60px;
}

.form-container1 {
  transform: translateY(-80%);
}

.second-font {
  margin-left: 13px;
}

.verification-flex {
  display: flex;
  flex-direction: column;
  align-items: flex-start;

  .img {
    margin-top: 10px;
    align-self: center;
  }
  .form {
    transform: translateY(15px);
    width: 90%;
  }
}
</style>
