<script lang="ts" setup>
import type { VbenFormSchema } from '@vben/common-ui';
import type { Recordable } from '@vben/types';

import { h, computed, ref } from 'vue';

import { AuthenticationRegister, z } from '@vben/common-ui';
import { $t } from '@vben/locales';
import { Button, message } from 'ant-design-vue';

import type { AuthApi } from '#/api';
import { useAuthStore } from '#/store';
import { sendEmailCodeApi } from '#/api';

defineOptions({ name: 'Register' });

const authStore = useAuthStore();
const formValues = ref<Recordable<any>>({});
const sendingCode = ref(false);
const countdown = ref(0);

function getSendCodeBtnText() {
  if (sendingCode.value) return '发送中...';
  if (countdown.value > 0) return `${countdown.value}s`;
  return '发送验证码';
}

function isSendCodeBtnDisabled() {
  if (sendingCode.value || countdown.value > 0) return true;

  const { username, password, confirmPassword, email } = formValues.value || {};

  if (!username) return true;
  if (username.length < 1 || username.length > 10) return true;
  if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(username)) return true;

  if (!password) return true;
  if (password.length < 6 || password.length > 20) return true;

  if (!confirmPassword) return true;
  if (confirmPassword !== password) return true;

  if (!email) return true;
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) return true;

  return false;
}

function validateAllFields() {
  const { username, password, confirmPassword, email } = formValues.value || {};

  if (!username) {
    message.warning('请输入用户名');
    return false;
  }
  if (username.length < 1 || username.length > 10) {
    message.warning('用户名必须是1-10个字符');
    return false;
  }
  if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(username)) {
    message.warning('用户名只能包含字母、数字和中文');
    return false;
  }

  if (!password) {
    message.warning('请输入密码');
    return false;
  }
  if (password.length < 6 || password.length > 20) {
    message.warning('密码必须是6-20位');
    return false;
  }

  if (!confirmPassword) {
    message.warning('请确认密码');
    return false;
  }
  if (confirmPassword !== password) {
    message.warning('两次密码输入不一致');
    return false;
  }

  if (!email) {
    message.warning('请输入邮箱');
    return false;
  }
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
    message.warning('请输入有效的邮箱地址');
    return false;
  }

  return true;
}

async function handleSendCode() {
  if (!validateAllFields()) return;

  if (sendingCode.value || countdown.value > 0) return;

  const { email } = formValues.value || {};

  try {
    sendingCode.value = true;
    await sendEmailCodeApi({ email, type: 'REGISTER' });
    message.success('验证码已发送，请查收邮箱');

    countdown.value = 60;
    const timer = setInterval(() => {
      countdown.value--;
      if (countdown.value <= 0) {
        clearInterval(timer);
      }
    }, 1000);
  } catch (error: any) {
    console.error('发送验证码失败:', error);
  } finally {
    sendingCode.value = false;
  }
}

const formSchema = computed((): VbenFormSchema[] => {
  return [
    {
      component: 'VbenInput',
      componentProps: {
        placeholder: '请输入用户名',
      },
      fieldName: 'username',
      label: '用户名',
      rules: z
        .string()
        .min(1, { message: '请输入用户名' })
        .regex(/^[a-zA-Z0-9\u4e00-\u9fa5]+$/, { message: '用户名只能包含字母、数字和中文' })
        .max(10, { message: '用户名最多10个字符' }),
    },
    {
      component: 'VbenInputPassword',
      componentProps: {
        passwordStrength: true,
        placeholder: $t('authentication.password'),
      },
      fieldName: 'password',
      label: $t('authentication.password'),
      renderComponentContent() {
        return {
          strengthText: () => $t('authentication.passwordStrength'),
        };
      },
      rules: z
        .string()
        .min(6, { message: '密码至少6位' })
        .max(20, { message: '密码最多20位' }),
    },
    {
      component: 'VbenInputPassword',
      componentProps: {
        placeholder: $t('authentication.confirmPassword'),
      },
      dependencies: {
        rules(values) {
          const { password } = values;
          return z
            .string({ required_error: $t('authentication.passwordTip') })
            .min(1, { message: $t('authentication.passwordTip') })
            .refine((value) => value === password, {
              message: $t('authentication.confirmPasswordTip'),
            });
        },
        triggerFields: ['password'],
      },
      fieldName: 'confirmPassword',
      label: $t('authentication.confirmPassword'),
    },
    {
      component: 'VbenInput',
      componentProps: {
        placeholder: '请输入邮箱',
      },
      fieldName: 'email',
      label: '邮箱',
      rules: z.string().min(1, { message: '请输入邮箱' }).email({ message: '请输入有效的邮箱地址' }),
      dependencies: {
        triggerFields: ['username', 'password', 'confirmPassword', 'email'],
        trigger(values) {
          formValues.value = values;
        },
      },
    },
    {
      component: 'VbenInput',
      componentProps: {
        placeholder: '请输入6位验证码',
        maxlength: 6,
        showCount: true,
      },
      fieldName: 'code',
      label: '验证码',
      suffix: () =>
        h(
          Button,
          {
            disabled: isSendCodeBtnDisabled(),
            loading: sendingCode.value,
            size: 'small',
            onClick: handleSendCode,
          },
          () => getSendCodeBtnText(),
        ),
      rules: z
        .string()
        .min(1, { message: '请输入验证码' })
        .length(6, { message: '验证码必须是6位' }),
    },
    {
      component: 'VbenCheckbox',
      fieldName: 'agreePolicy',
      renderComponentContent: () => ({
        default: () => [$t('authentication.agree')],
      }),
      rules: z.boolean().refine((value) => !!value, {
        message: $t('authentication.agreeTip'),
      }),
    },
  ];
});

async function handleSubmit(value: Recordable<any>) {
  const { confirmPassword, agreePolicy, ...params } = value;
  await authStore.authRegister(params as AuthApi.RegisterParams);
}
</script>

<template>
  <AuthenticationRegister
    :form-schema="formSchema"
    :loading="authStore.loginLoading"
    @submit="handleSubmit"
  />
</template>
