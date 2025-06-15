import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        redirect: '/plan/list'
    },
    {
        path: '/plan/list',
        name: 'PlanList',
        component: () => import('@/views/plan/list.vue')
    },
    {
        path: '/plan/add',
        name: 'PlanAdd',
        component: () => import('@/views/plan/add.vue')
    },
    {
        path: '/plan/edit/:id',
        name: 'PlanEdit',
        component: () => import('@/views/plan/edit.vue')
    },
    {
        path: '/plan/detail/:id',
        name: 'PlanDetail',
        component: () => import('@/views/plan/detail.vue')
    }
]

const router = new VueRouter({
    routes
})

export default router