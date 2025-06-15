import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        // 可扩展全局状态，如用户信息、权限等
        user: {
            name: '管理员'
        }
    },
    mutations: {
        SET_USER(state, user) {
            state.user = user
        }
    },
    actions: {
        setUser({ commit }, user) {
            commit('SET_USER', user)
        }
    },
    modules: {}
})