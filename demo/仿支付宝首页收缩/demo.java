@Override
public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    //垂直方向偏移量
    int offset = Math.abs(verticalOffset);
    //最大偏移距离
    int scrollRange = appBarLayout.getTotalScrollRange();
    if (offset <= scrollRange / 2) {//当滑动没超过一半，展开状态下toolbar显示内容，根据收缩位置，改变透明值
        toolbarOpen.setVisibility(View.VISIBLE);
        toolbarClose.setVisibility(View.GONE);
        //根据偏移百分比 计算透明值
        float scale2 = (float) offset / (scrollRange / 2);
        int alpha2 = (int) (255 * scale2);
        bgToolbarOpen.setBackgroundColor(Color.argb(alpha2, 25, 131, 209));
    } else {//当滑动超过一半，收缩状态下toolbar显示内容，根据收缩位置，改变透明值
        toolbarClose.setVisibility(View.VISIBLE);
        toolbarOpen.setVisibility(View.GONE);
        float scale3 = (float) (scrollRange  - offset) / (scrollRange / 2);
        int alpha3 = (int) (255 * scale3);
        bgToolbarClose.setBackgroundColor(Color.argb(alpha3, 25, 131, 209));
    }
    //根据偏移百分比计算扫一扫布局的透明度值
    float scale = (float) offset / scrollRange;
    int alpha = (int) (255 * scale);
    bgContent.setBackgroundColor(Color.argb(alpha, 25, 131, 209));
}
