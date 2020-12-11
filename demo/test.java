class aa {
    public void test(final int b) {
        final int a = 10;
        new Thread() {
            public void run() {
                System.out.println(a);
                System.out.println(b);
            }
        }.start();
    }
}