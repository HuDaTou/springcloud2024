export interface LoginParams {
  username: string
  password: string
  type?: 'account'
}

export interface LoginMobileParams {
  mobile: string
  code: string
  type: 'mobile'
}

export interface LoginResultModel {
  code: number
  msg: string
  token: string
  expire: string
}

export function loginApi(params: LoginParams | LoginMobileParams) {
  const formData = new URLSearchParams()
  
  if ('username' in params) {
    formData.append('username', params.username)
    formData.append('password', params.password || '')
  }
  else {
    formData.append('mobile', params.mobile || '')
    formData.append('code', params.code || '')
  }
  
  return usePost<LoginResultModel, string>('/cloud-auth/auth/login', formData.toString(), {
    token: false,
    loading: true,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'X-Client-Type': 'Backend',
    },
  })
}

export function logoutApi() {
  return useGet('/cloud-auth/auth/logout')
}