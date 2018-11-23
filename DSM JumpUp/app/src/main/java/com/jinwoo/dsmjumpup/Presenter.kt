package com.jinwoo.dsmjumpup

class Presenter: Contract.Presenter{
    private var view: Contract.View? = null
    override fun setView(view: Contract.View) {
        this.view = view
    }

}