## Android轻量级Layout Inspector工具

[![](https://jitpack.io/v/niuxiaowei/LayoutInspector.svg)](https://jitpack.io/#niuxiaowei/LayoutInspector)


#### 效果图
![layout inspector工具效果](/images/layoutinspector.gif)

#### 为什么要做这个工具
如果有更好的轮子用，谁愿意造轮子。

在21年年初的时候，我被调到公司的另外一个项目，这个项目有几个特点：历史非常的悠久，使用的技术落后，项目工程复杂，代码量巨大，界面布局层次特别深。
这么庞大的项目对于我这个“新人”来说，找个功能的实现，或者找某个view的点击事件是在哪实现的，或者某个view所属的layout是啥等等 都很困难。

通过阅读项目的源码来找具体功能点是可行的（比如找某个view的点击事件在哪，大致步骤是先找到Activity进而找到布局layout文件，进而找到view的id，再去找这个id在什么地方设置了click事件），这种方法确实可行，但是效率低下。

那有没有高效率的方法呢，我想到了android studio（以下简称as）自带的layout inspector工具，通过它可以知道界面中view的id及其他元素信息，通过view的id可以在as中全局搜索，从而定位到具体的layout和代码位置（这只是基于全局只存在一个唯一的id情况）。

当我使用as的layout inspector工具时候，现实给了我重重的一棒，不知道是我们app复杂的原因，as的layout inspector在检测中经常会导致app死掉（检测工具报oom导致app死掉），并且检测过程中经常会出现检测不到设备的问题或者一直处于检测中等问题（不可否认as后面的版本肯定会把这些问题都修好的）。

因此我就想自己是否能做一个这样的工具，这个工具可以 **直接在手机上显示view的属性（宽高，坐标，id值等等，甚至开发者自己可以扩展属性），不仅可以为开发人员提供服务，同时也可以为设计人员提供服务**

#### 轻量级Layout Inspector工具都有哪些功能

##### 1.菜单
菜单会显示在每个activity上的左上部分，是可以拖动的（若阻碍了当前界面操作）

**显示** 菜单：点击它后，会把当前界面的所有view的边界，pading，margin 显示出来，这时候文本变为 **隐藏**。这时候点击每个view都会显示view信息的界面，view的真正点击事件不会触发（**若需要触发，再次点击该菜单，隐藏上面的界面**），如下图

![view的margin, padding](https://upload-images.jianshu.io/upload_images/1504173-5fa9d0ad95c7c849.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**更多** 菜单

![更多菜单内容](https://upload-images.jianshu.io/upload_images/1504173-a9d4a29b12cc6a71.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

padding，margin显示样式说明
屏幕的宽 高
当前activity，activity layout的信息
当前的fragment以及总共显示了多少fragment
pading，margin等尺寸的单位：可以选择dp或px
ViewGroup显示View检测器：点击是表面显示它的检测器， 点击否 不显示
显示view margin：是 绘制所有view的margin， 否 不绘制
显示view padding：是 绘制所有view的padding， 否 不绘制


##### 2.显示view的margin，padding

![view的margin, padding](https://upload-images.jianshu.io/upload_images/1504173-5fa9d0ad95c7c849.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![margin](https://upload-images.jianshu.io/upload_images/1504173-d948a47f75e67406.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

上图  **浅蓝色的箭头**，代表view的margin，箭头的朝向分别代表不同方向的margin如left, right, top ,bottom，箭头的长度越大代表margin值越大

![padding.png](https://upload-images.jianshu.io/upload_images/1504173-3da7bbdade17840c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

上图 **浅蓝色的矩形框**，代表view的padding，处于view的上部分代表padding top，下部分代表padding bottom，左部分代表padding left，右部分代表padding right。矩形框高度越大代表padding值越大。

**红色线条**，代表view的边界线

##### 3.显示view的属性

![view的id,size,坐标,布局,click信息](https://upload-images.jianshu.io/upload_images/1504173-4e2eb00b761240e1.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

图中元素说明：
绿色框代表当前被检测的view
弹出的深蓝色框包含：**控件属性**和**控件层级**
文本为 **可点击** 背景框为红色的项：代表该项是可以点击的

3.1 显示view的id,类名

3.2 **view检查器：点击不现实该view的检查器**  它主要用于：view层级出现覆盖的时，想查看最底层view的属性时，可以点击此项，不显示最上层的view的检查器。

3.3 **所属布局名称，布局被inflate位置**  这两项非常的有用，前者可以显示当前的view所属的layout名称，后者可以显示layout布局被inflate的位置（类名#方法名#行号），甚至可以在as的logcat中搜索LayoutInspector的tag，可以直接点击这个log跳转到**具体代码位置**

3.4 **是否设置onClickListener，onClickListener位置** 这两项在查找view的onClickListener信息的时候非常有用，**onClickListener位置**包含 类名#方法名#行号 信息（可以在as的logcat中搜索LayoutInspector的tag，可以直接点击这个log跳转到**具体代码位置**）

3.5 **是否设置onLongClickListener，onLongClickListener位置** 这两项在查找view的onLongClickListener信息的时候非常有用，**onLongClickListener位置** 同 **onClickListener位置**

3.6 显示size, layout_width, layout_height, 坐标(x , y)

![padding, margin, activity](https://upload-images.jianshu.io/upload_images/1504173-e2bea64b6076b622.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

3.7 **显示view的padding，margin信息** 这两项也是非常有用，在和设计同学走查ui的时候，开发同学深有体会，明明是按设计的尺寸写的值，设计同学会觉得不对，这时候咱们就可以用数据来说话了。同时也为设计同学带来了方便，想检查哪个view的信息时候，自己就可以操作了。

**动态修改view属性** 是一个小功能  ，比如点击paddingTop这一项，会弹出一个 对话框，可以动态的修改paddingTop的值，这个功能可以**提高 与设计同学走查ui的效率**（不需要重复 在代码或布局文件中修改属性值，启动app查看效果这个过程），动态修改到合适的值后，再去代码或布局文件中修改，启动查即可）

3.8 对于TextView显示textcolor，和textsize  同样它们也可以适用 **动态修改view属性** 这个功能

3.9 显示当前activity和activity layout信息

##### 4.显示view的层级（点击上面的 view的margin, padding图 中的任意view会出现下面的图）

![控件层级](https://upload-images.jianshu.io/upload_images/1504173-53e2470404f8760b.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

上图元素说明：
绿色字体：代表当前被查看的view

由于担心显示的view实在过多的原因，暂时只会把当前view的子view和它的兄弟view给显示出来，当前view的祖先view不会罗列这些信息。
点击对应项的view会显示该view的属性和层级信息


##### 5.显示Dialog的信息

![dialog](https://upload-images.jianshu.io/upload_images/1504173-ef9c965095270349.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Dialog的处理要稍微特殊一些，为了简洁化，把 **更多** 菜单去掉了，其他的操作，显示的属性信息都与上面介绍的一样。

##### 6.显示PopupWindow的信息
在这就不展示图片了，可以查看文章开始的gif图


#### 接入

1.工程根目录下的build.gradle文件添加下面内容

```
buildscript {

    repositories {
        //replacemethod库
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        //replacemethod的类，主要对inflate，setOnclickListener等方法进行替换
        classpath "com.github.niuxiaowei:ReplaceMethod:1.0.4"
    }
}

allprojects {
    repositories {
        //LayoutInspector工具地址
        maven { url 'https://jitpack.io' }
    }
}
```

2.在app下的build.gradle文件添加下面内容

```
apply plugin: 'ReplaceMethodPlugin'

dependencies {
  debugImplementation 'com.github.niuxiaowei.LayoutInspector:layoutinspector:2.0.1'
  releaseImplementation 'com.github.niuxiaowei.LayoutInspector:layoutinspector-no-op:2.0.1'
}

replaceMethod {
    open = true //这里如果设置为false,则会关闭插桩
    openLog false
    replaceByMethods {
        //对inflate方法进行替换，用来显示view的layout被inflate的位置信息
        register {
            replace {
                invokeType "ins"
                className "android.view.LayoutInflater"
                methodName "inflate"
                desc "(int,android.view.ViewGroup)android.view.View"
            }
            by {
                className = "com.mi.layoutinspector.replacemethod.LayoutInflaterProxy"
                addExtraParams = true
            }
        }

        register {
            replace {
                invokeType "static"
                className "android.view.View"
                methodName "inflate"
                desc "(android.content.Context,int,android.view.ViewGroup)android.view.View"
                ignoreOverideStaticMethod true
            }
            by {
                className = "com.mi.layoutinspector.replacemethod.LayoutInflaterProxy"
                addExtraParams  true
            }
        }

        register {
            replace {
                invokeType "ins"
                className "android.view.LayoutInflater"
                methodName "inflate"
                desc "(int,android.view.ViewGroup,boolean)android.view.View"
            }
            by {
                className = "com.mi.layoutinspector.replacemethod.LayoutInflaterProxy"
                addExtraParams =  true
            }
        }

        //若想对dialog进行检测，需要添加下面配置
        register {
            replace {
                invokeType "ins"
                className "android.app.Dialog"
                methodName "show"
            }
            by {
                className = "com.mi.layoutinspector.replacemethod.DialogProxy"
            }
        }

        //若想对PopupWindow进行检测，需要添加下面配置
        register {
            replace {
                invokeType "ins"
                className "android.widget.PopupWindow"
                methodName "showAsDropDown"
                desc "(android.view.View)"
            }
            by {
                className = "com.mi.layoutinspector.replacemethod.PopupWindowProxy"
            }
        }

        register {
            replace {
                invokeType "ins"
                className "android.widget.PopupWindow"
                methodName "showAsDropDown"
                desc "(android.view.View,int,int)"
            }
            by {
                className = "com.mi.layoutinspector.replacemethod.PopupWindowProxy"
            }
        }

        register {
            replace {
                invokeType "ins"
                className "android.widget.PopupWindow"
                methodName "showAsDropDown"
                desc "(android.view.View,int,int,int)"
            }
            by {
                className = "com.mi.layoutinspector.replacemethod.PopupWindowProxy"
            }
        }

        register {
            replace {
                invokeType "ins"
                className "android.widget.PopupWindow"
                methodName "showAtLocation"
                desc "(android.view.View,int,int,int)"
            }
            by {
                className = "com.mi.layoutinspector.replacemethod.PopupWindowProxy"
            }
        }

        //对setOnClickListener方法进行替换，在view检测器中显示click的位置信息
        register {
            replace {
                invokeType "ins"
                className "android.view.View"
                methodName "setOnClickListener"
                desc "(android.view.View\$OnClickListener)"
            }
            by {
                className = "com.mi.layoutinspector.replacemethod.OnClickListenerProxy"
                addExtraParams =  true
            }
        }

        //对setOnLongClickListener方法进行替换，在view检测器中显示click的位置信息
        register {
            replace {
                invokeType "ins"
                className "android.view.View"
                methodName "setOnLongClickListener"
                desc "(android.view.View\$OnLongClickListener)"
            }
            by {
                className = "com.mi.layoutinspector.replacemethod.OnClickListenerProxy"
                addExtraParams =  true
            }
        }

    }
}
```

3.扩展view的属性

调用下面的方法，可以在检测器中显示扩展的view属性，具体可以参考demo中的例子

```
LayoutInspector.INSTANCE.register(IViewAttributeCollector collector)

```

如下代码对TextView的文本进行动态修改（检测器显示的时候就会显示该项）：

```
LayoutInspector.INSTANCE.register(new IViewAttributeCollector() {
            @Nullable
            @Override
            public List<ViewAttribute> collectViewAttributes(@NotNull View inspectView, @NotNull IViewInspector IViewInspector) {
                return null;
            }

            @Nullable
            @Override
            public ViewAttribute collectViewAttribute(@NotNull View inspectView, @NotNull IViewInspector IViewInspector) {
                if (inspectView instanceof TextView) {
                    ViewAttribute viewAttribute = new ViewAttribute("修改TextView内容", "点击进行修改", v -> {
                        IViewInspector.hideViewInfosPopupWindown();
                        TextView textView = (TextView) inspectView;
                        final EditText editText = new EditText(inspectView.getContext());
                        AlertDialog.Builder inputDialog =
                                new AlertDialog.Builder(inspectView.getContext());
                        inputDialog.setTitle("输入内容").setView(editText);
                        inputDialog.setPositiveButton("确定修改",
                                (dialog, which) -> {
                                    String msg = editText.getText().toString();
                                    if (!TextUtils.isEmpty(msg)) {
                                        textView.setText(msg);
                                    }
                                }).show();
                    });
                    return viewAttribute;
                }
                return null;

            }
        });
```

该库使用了[对方法进行替换库](https://github.com/niuxiaowei/ReplaceMethod)

**我的公众号**
![我的微信公众号](https://user-images.githubusercontent.com/3078303/148639094-a57cf897-eec6-4d79-a724-42b36e742b96.jpg)
