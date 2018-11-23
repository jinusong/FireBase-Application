package com.jinwoo.dsmjumpup

interface Contract{
    interface View {

    }
    interface Presenter{
        fun setView(view: View){}
    }
}