# 百星快族直播 Android 客户端

## 项目概述

百星快族直播 Android 客户端是一个提供直播服务的移动应用，具有用户登录、青少年模式、视频播放等核心功能。应用采用了模块化的架构设计，各个功能模块之间通过清晰的接口进行交互。

## 项目架构

### 整体架构

项目采用 MVVM (Model-View-ViewModel) 架构模式，结合 Android Jetpack 组件，实现了视图与业务逻辑的分离。主要分为以下几层：

- **表现层**：包括 Activity、Fragment 和各种自定义 View
- **业务逻辑层**：包括 ViewModel、各种业务管理器和任务处理类
- **数据层**：包括本地数据管理和网络请求处理

### 包结构

```
com.baixingkuaizu.live.android
├── activity        // 活动类
├── adatperandroid  // 适配器相关类
├── base            // 基础类
├── busiess         // 业务逻辑类
│   ├── localdata   // 本地数据管理
│   ├── manager     // 管理器类
│   ├── proxy       // 代理类
│   ├── router      // 路由管理
│   ├── task        // 任务处理
│   ├── teenmode    // 青少年模式
│   └── thread      // 线程管理
├── dialog          // 对话框类
├── fragment        // 片段类
├── http            // 网络请求
├── utils           // 工具类
├── viewmodel       // 视图模型
└── widget          // 自定义控件
    ├── loading     // 加载控件
    ├── toast       // 提示控件
    └── web         // Web相关控件
```

## 核心模块介绍

### 1. 应用入口

**Baixing_MyApp**: 应用程序全局入口类，提供全局上下文访问。

### 2. 基础组件

- **Baixing_BaseActivity**: 基础活动类，所有Activity的父类，提供生命周期管理、窗口设置、震动反馈等通用功能。集成了GoRouter路由管理，在Activity的创建和销毁时进行回调，并提供了View扩展方法setWindowListener用于处理系统窗口插入，支持全面屏和刘海屏适配。项目中所有Activity都继承自此类以保持一致的行为。另外，提供vibrateOnce方法便于触发设备震动反馈，增强用户交互体验。
- **Baixing_BaseFragment**: Fragment基类，所有Fragment的父类，提供生命周期管理、页面切换等通用功能。定义了Fragment的基本行为规范，确保所有子类有一致的表现。使用ViewBinding进行视图绑定，简化视图操作，提高代码可读性。
- **Baixing_Entity**: 所有实体类的基类。

### 3. 路由管理

**Baixing_GoRouter**: 负责处理应用内页面跳转和Activity生命周期管理，维护Activity栈。

### 4. 本地数据管理

**Baixing_LocalDataManager**: 负责处理应用本地数据的存储和读取，如隐私政策同意状态、登录令牌、青少年模式设置等。

### 5. 线程管理

- **Baixing_ConcurrentThreadPool**: 并发线程池，管理应用内的线程资源。
- **Baixing_Thread**: 线程封装类，提供任务执行和中断功能。

### 6. 任务管理

- **Baixing_TaskManager**: 任务管理器基类，提供任务的添加、删除、查询和清理等通用功能。
- **Baixing_BaseTask**: 任务基类，所有任务类的父类，提供任务生命周期管理的基本方法。

### 7. 登录系统

- **Baixing_LoginActivity**: 登录活动页面，使用ViewBinding进行视图绑定，负责加载登录相关的Fragment并处理返回键逻辑。
- **Baixing_SelectLoginFragment**: 登录选择界面，负责展示不同的登录方式选项，并在启动时自动检查登录状态，如果已登录则直接跳转到主页面。
- **Baixing_LoginFragment**: 登录表单界面，实现手机号验证码登录功能，包括发送验证码、验证用户协议同意状态、处理登录请求等。该页面与Baixing_LoginViewModel协同工作，处理登录业务逻辑并观察登录结果。
- **Baixing_LoginViewModel**: 登录相关的视图模型，处理登录业务逻辑。

### 8. 青少年模式

- **Baixing_TeenModeActivity**: 青少年模式活动页面，是青少年模式相关Fragment的容器，使用ViewBinding进行视图绑定，负责加载青少年模式相关的Fragment并处理返回键逻辑。
- **Baixing_TeenModeFragment**: 青少年模式设置页面，负责启用青少年模式和设置监护密码。当用户点击启用青少年模式按钮时，会弹出密码设置对话框，要求设置并确认监护密码。设置成功后会将密码保存到LocalDataManager并跳转到播放列表页面。如果青少年模式已启用，会直接跳转到播放列表页面。
- **Baixing_TeenPlayListFragment**: 青少年模式播放列表页面，展示适合青少年的内容并管理使用时间限制。该页面实现了标签筛选、内容展示、使用时间监控等功能。使用时间到达限制后，会显示密码验证对话框，要求输入监护密码才能继续使用。该页面使用ViewBinding进行视图绑定，Handler处理定时任务，与多个对话框类和LocalDataManager协同工作。当用户点击视频项时，会跳转到Baixing_VideoPlayerActivity播放相应视频。
- **Baixing_TeenPlayListAdapter**: 青少年模式播放列表适配器。
- **Baixing_VideoData**: 视频数据模型类，包含视频ID、标题、作者、封面URL、播放时长、分类标签、视频URL等信息，为视频播放提供必要的数据支持。
- **Baixing_TeenModeExtendTimeDialog**: 青少年模式使用时间延长对话框，当用户使用时间达到限制时（默认40分钟），显示该对话框进行监护密码验证。该对话框与Baixing_LocalDataManager密切配合，通过后者验证监护密码的正确性并记录验证时间。验证成功后会重置计时器并允许继续使用，失败则会提示密码错误。为了确保青少年保护机制的有效性，该对话框禁用了取消按钮，且不允许通过返回键或点击外部区域关闭。
- **Baixing_VideoPlayerActivity**: 视频播放器活动页面，负责播放青少年模式中用户点击的视频内容。该活动使用VideoView实现视频播放功能，支持视频标题显示、加载进度条以及错误处理机制。通过Intent接收视频标题和URL等信息，并在生命周期事件中管理视频播放状态，如在页面暂停时暂停播放，页面销毁时释放播放资源。该活动实现了一个轻量级但功能完整的视频播放器，与青少年模式的内容保护机制无缝集成。

### 9. Web功能

- **Baixing_WebActivity**: Web活动页面，使用ViewBinding进行视图绑定，负责加载和显示网页内容，如隐私政策和用户协议。
- **Baixing_WebViewManager**: WebView管理器，负责WebView的创建、获取、更新和销毁。
- **Baixing_WebViewWrapper**: WebView包装类，提供WebView的基本功能。

### 10. 对话框

- **Baixing_PrivacyDialog**: 隐私政策对话框，用于显示和同意隐私政策。
- **Baixing_TeenModeDialog**: 青少年模式提示对话框，用于引导用户进入青少年模式。
- **Baixing_ExitDialog**: 退出对话框，用于验证监护密码以退出青少年模式。该对话框提供密码输入界面，用户需要输入正确的监护密码才能退出青少年模式。验证通过后会清除密码、禁用青少年模式并通知调用方。该对话框使用ViewBinding进行视图绑定，与Baixing_LocalDataManager类配合，负责密码验证和青少年模式状态修改。对话框采用CenterToast显示操作结果，并提供取消功能允许用户放弃退出操作。
- **Baixing_TeenModeExtendTimeDialog**: 青少年模式使用时间延长对话框，用于验证监护密码以继续使用，当用户使用时间达到上限时显示。此对话框与Baixing_LocalDataManager类协同工作，负责密码验证和时间记录。对话框采用ViewBinding进行视图绑定，使用CenterToast显示验证结果。为确保青少年保护机制的严格执行，对话框禁用了取消按钮，且不允许通过返回键或点击外部区域关闭，必须输入正确的监护密码才能继续使用。

### 11. 适配器和UI工具

- **Baixing_AdapterHelper**: 适配器辅助类，提供点击事件防抖和尺寸转换功能。通过扩展函数方式提供setClick方法，该方法封装了防抖动点击监听，防止用户快速多次点击导致的重复操作。还提供dp2px转换方法，用于在代码中动态计算像素尺寸。
- **Baixing_DebounceClickListener**: 防抖点击监听器，用于防止用户在短时间内多次点击同一控件导致的重复操作问题。该监听器通过记录上次点击时间，并在设定的时间间隔内（默认500毫秒）忽略重复点击，确保按钮或其他可点击控件的操作只会被执行一次。结合Baixing_AdapterHelper类中的setClick扩展函数使用，极大改善了应用的用户体验。

### 12. 代理和工具类

- **Baixing_ActivityProxy**: Activity代理类，负责管理Activity的生命周期和相关状态。提供bind和unbind方法，在Activity创建和销毁时调用，确保资源的正确管理和释放。这种代理模式的使用简化了Activity类的实现，将部分功能解耦，便于维护和测试。
- **Baixing_PermissionCheck**: 权限检查类，负责检查和请求应用所需的各种权限。该类提供了统一的权限申请入口，简化了权限管理流程，并通过回调机制将权限结果返回给调用方。主要在应用启动阶段使用，确保获取必要的运行权限。

### 13. 视图扩展功能

- **View扩展函数**: 项目中为View类添加了多个扩展函数，如`setWindowListener()`，用于为View设置窗口监听事件，主要用于Activity和Fragment中的根View，简化了窗口事件处理。这些扩展函数极大地简化了代码，提高了可读性和可维护性。
- **UI控件的统一处理**: 通过扩展函数和工具类，实现了对UI控件的统一处理，如点击事件防抖、尺寸转换等，确保了应用在不同设备上的一致体验和流畅操作。

## 类关系图

### 活动类关系

```
Baixing_BaseActivity
├── Baixing_MainActivity
├── Baixing_LoginActivity
├── Baixing_TeenModeActivity
├── Baixing_SplashActivity
├── Baixing_WebActivity
└── Baixing_VideoPlayerActivity
```

### Fragment类关系

```
Baixing_BaseFragment
├── Baixing_LoginFragment - 处理登录表单和验证码登录逻辑
├── Baixing_SelectLoginFragment - 提供登录方式选择界面
├── Baixing_TeenModeFragment - 管理青少年模式设置和密码设定
└── Baixing_TeenPlayListFragment - 展示青少年内容并管理使用时间
```

### 对话框类关系

```
Dialog
├── Baixing_PrivacyDialog
├── Baixing_TeenModeDialog
├── Baixing_ExitDialog
└── Baixing_TeenModeExtendTimeDialog
```

### 任务管理类关系

```
Baixing_BaseTask
└── 各种具体任务实现

Baixing_TaskManager<T: Baixing_BaseTask>
├── Baixing_LoginTaskManager
└── Baixing_SendVerficationCodeTaskManager
```

### 线程管理类关系

```
Baixing_ConcurrentThreadPool
└── 管理 Baixing_Thread 实例
```

### UI工具类关系

```
View.OnClickListener
└── Baixing_DebounceClickListener
```

### 代理类关系

```
Baixing_ActivityProxy
└── 管理 Baixing_BaseActivity 生命周期
```

### 视图扩展关系

```
View
├── 扩展函数 setWindowListener()
└── 其他UI相关扩展函数
```

## 功能流程

### 1. 应用启动流程

1. 应用启动 -> Baixing_SplashActivity
2. 检查隐私政策状态 -> 未同意则显示隐私政策对话框
3. 检查登录状态 -> 未登录则跳转到 Baixing_LoginActivity
4. 已登录 -> 跳转到 Baixing_MainActivity

### 2. 青少年模式流程

1. 进入主界面 -> 检查青少年模式状态
2. 未启用 -> 显示青少年模式对话框
3. 已启用 -> 跳转到 Baixing_TeenModeActivity
4. 在青少年模式中 -> 显示适合青少年的内容，限制使用时间
5. 使用时间到限制（40分钟）-> 显示Baixing_TeenModeExtendTimeDialog -> 输入正确监护密码 -> 重置使用时间 -> 继续使用
6. 点击视频 -> 跳转到Baixing_VideoPlayerActivity -> 播放视频

### 3. 登录流程

1. 进入登录界面 -> 选择登录方式（Baixing_SelectLoginFragment）
2. 手机号登录（Baixing_LoginFragment）-> 输入手机号 -> 发送验证码 -> 验证 -> 登录成功
3. 登录成功 -> 保存登录令牌 -> 跳转到主界面

## 开发规范

1. 类名前缀统一为 "Baixing_"，便于识别项目中的类
2. 方法名前缀统一为 "baixing_"，便于识别项目中的方法
3. 成员变量前缀统一为 "mBaixing_"，便于识别项目中的成员变量
4. 所有Fragment类继承自Baixing_BaseFragment基类
5. Fragment与Activity之间通过接口或ViewModel进行通信
6. 所有UI操作使用ViewBinding进行视图绑定，不使用findViewById()
7. 使用CenterToast替代Android原生Toast进行消息提示
8. 命名资源文件时使用baixing_前缀
9. 使用dimen资源定义尺寸，格式为@dimen/dp.XX和@dimen/sp.XX
10. 所有Activity类继承自Baixing_BaseActivity基类
11. 所有Activity和Fragment类都使用ViewBinding进行视图绑定，提高代码可读性和类型安全性
12. 所有UI相关的尺寸使用@dimen资源引用，确保界面在不同设备上的一致性
13. 支持中文输入和显示，确保用户界面友好