# 阶段二项目使用说明

这是能运行的模拟监测基础版本。真实 ESP32、传感器、蜂鸣器尚未接入。

## 先运行

双击本目录的 `run.cmd`。第一次需要联网下载 Maven 和测试依赖。程序会先构建并运行测试，再打开界面。

如果窗口一闪而过，请在该目录打开 PowerShell，运行 `./run.cmd` 查看错误。

在 IntelliJ 中打开本目录，按 Maven 工程导入，使用 JDK 17 或更高版本，运行 `edu.northeastern.overheat.Main`。

## 演示

- HEATING：模拟升温，达到 60 度显示 Warning，达到 80 度显示 Critical。序列循环。
- INVALID：模拟无效数据，出现 Fault。
- MISSING：模拟传感器断流，10 秒没有有效读数后出现 Fault。
- Start 启动；Stop 停止。先停止，再切换场景。
- CSV 保存在 `data` 目录。显示的旧温度不是新读数，故障时注意提示文字。

## 运行测试

```powershell
.\mvnw.cmd test
```

## 没有界面的命令行演示

```powershell
.\run.cmd --demo
```

演示使用加速的虚拟秒数，不需要等待，也不代表测量了真实硬件的性能。

## 提交前需要你们自己完成

1. 三个人读懂并测试代码，各自修改、提交实际工作。
2. README 填写真实 GitHub 用户名、分工和贡献。
3. 确认 Phase 1 恢复余量、升温速率的具体参数。这两个规则当前未实现，不能写成已完成。
4. 创建公开 GitHub 仓库，邀请组员，按提交清单操作。

详细英文 README 用于项目说明；`docs` 里有架构图、测试计划、建议分工和 Canvas 评论模板。
