## About OkViewStub

主要是改进ViewStub 的使用体验问题
1. xml 设置 内容. 可指定 layout 或者 view class
2. 代码 inflate. 可inflate xml 内容 或者 直接设置 view
3. 支持 Android Studio 预览

## ViewStub 的问题：
使用不够方便。

1. 只能使用 xml 引用layout xml，不能直接使用 class，类似 fragment 的 class="a.b.c".
  
  实际上，需要填充的内容很可能是一个View 控件，如果要用 ViewStub, 就得使用一个 layout.xml 指向这个 View 类型，实在是多余。

2. 不能用代码设置view

实际开发，每个模块的接口更多的是返回一个 View，例如 DemoView, 而不是一个 layout.xml, 这时就无法使用 ViewStub，但是我又想按需设置View，通常只有两个做法： 
1. 使用 Layout 占位。
例如 FrameLayout 代替 ViewStub, 直接 addView(demoView);  
代价是xml 布局多了一个 层级 或者多了一个没用到的 FrameLayout

2. 用代码设置
`parent.addView(demoView), setLayoutParams, setMargin, setPadding, setBackground ...   `
代价是代码写布局, 可读性太差，一定会被同事吐槽

所改进版的ViewStub 需要改进的功能就很明确了。

## 改进方案： OkViewStub

1. xml 设置 内容. 可指定 layout 或者 view class
2. 代码 inflate. 可inflate xml 内容 或者 直接设置 view
3. 支持 Android Studio 预览

```xml
 <xyz.icodes.widget.OkViewStub
        android:id="@+id/stubA"
        android:layout_width="200dp"
        android:layout_height="64dp"
        android:layout_gravity="center"
        app:layout="@layout/demo"
        app:layout_class="com.example.myapplication.DemoView"
        />
```
```
1. app:layout 或者 app:layout_class 二选一，指定内容。或者不指定，用代码设置 viewStub.inflate(view)
2. viewstub.inflate() 或者 xml 不指定view，用代码设置 viewStub.inflate(your view) 
3. 其他：
    OkViewStub.isflated 查询是否设置过内容
    OkViewStub.contentView 当前内容View
```

## Demo Code
1. set view layout or class on xml

```xml
<!-- use layout -->
<xyz.icodes.widget.OkViewStub
    android:id="@+id/stubA"
    android:layout_width="200dp"
    android:layout_height="64dp"
    app:layout="@layout/demo" />

<!--  use android build in view -->
<xyz.icodes.widget.OkViewStub
    android:id="@+id/stubB"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:layout_class="SearchView" />

<!-- use custom view-->
<xyz.icodes.widget.OkViewStub
    android:id="@+id/stubC"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:layout_class="com.example.myapplication.DemoView" />

<!--  inflate by code -->
<xyz.icodes.widget.OkViewStub
    android:id="@+id/stubD"
    android:layout_width="100dp"
    android:layout_height="100dp" />
```

2. inflate content 

```kotlin
    stubA.inflate()
    stubB.inflate()
    stubC.inflate()

    // first content view
    var image = ImageView(this)
    image.setImageResource(R.mipmap.ic_launcher_round)
    stubD.inflate(image)

    image.postDelayed({
        // we can replace content view multiple times
        image = ImageView(this)
        image.setBackgroundResource(R.drawable.ic_launcher_background)
        image.setImageResource(R.drawable.ic_launcher_foreground)
        stubD.inflate(image)
    }, 2000)

  ```


## 参考

+ [ViewStub 源码](https://cs.android.com/android/platform/superproject/+/master:frameworks/base/core/java/android/view/ViewStub.java)
+ [OkViewStub 实现源码](https://github.com/xechoz/OkViewStub)
