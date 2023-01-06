package com.justin.mysightsecurity.socket;

class updateUIThread implements Runnable {
    private String msg;

    public updateUIThread(String str) {
        this.msg = str;
    }

    @Override
    public void run() {
        Global.text.setText(Global.text.getText().toString()+"Client Says: "+ msg + "\n");
    }
}
