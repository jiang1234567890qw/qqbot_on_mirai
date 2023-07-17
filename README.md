#qqbot_on_mirai
##这是一个什么项目？
这是一个基于[mirai](https://github.com/mamoe/mirai)的qqbot，通过与[mirai](https://github.com/mamoe/mirai)的[api-http](https://github.com/project-mirai/mirai-api-http)通信完成对机器人的操控。如果你想，你可以把这个项目clone下来，删去BotFunction文件夹下你不需要的代码，此时，这个项目就变成了一个与[mirai-api-http](https://github.com/project-mirai/mirai-api-http)通信的框架。
##我需要遵守什么限制吗？
然而并不用，这个仓库不遵循任何开源协议，任何人都可以拿去使用即使是商业用途。但因为上游项目是[mirai](https://github.com/mamoe/mirai)，你需要遵守一切[mirai](https://github.com/mamoe/mirai)的使用规定。
##这个项目为什么存在？
这个项目来自我的一个突发想法，想要写一个qq机器人。为了偷懒，我将大部分方法进行了模块化，这样在后期开发时我就能方便的自定义命令，顺带的，这个项目可以作为一个框架使用。
##项目的将来
关于将来，我暂时没有特别明了的预期。目前而言，只有我一个人在开发，且我是一个高中生，开学以后很难有大把时间来继续开发，这个项目被废弃是十分有可能的。
##项目的使用
项目暂时没有完成对文件读写的支持，因此所有东西基本靠硬编码完成。如过你想使用你至少需要修改RunBot文件下的`url`,`key`,`qq`三个变量（常量），其中url是mirai-api-http的地址与端口，需要带上`http://`或`https://`，key则是mirai-api-http生成的/手动设置的verifyKey，qq则是需要使用的bot的qq号。
目前来讲，只能够使用一个bot，除非多开。在未来的提交中会增加多bot的支持和配置文件的支持。并且会尝试一些手段来提高并发能力（bot并不需要太多算力，只是挖个坑）
项目的编写与测试在阿里巴巴的[drangonwell-jdk 17](https://github.com/dragonwell-project/dragonwell17)上，一般能兼容openjdk与oracle jdk，只保证能在jdk17上能正常编译及运行
##表达支持
如果你对这个项目比较满意，还请点亮star，或参与到这个项目的完善中来。也可以自己根据这个框架进行开发，如果你愿意，还请在一个相对明细的位置写明你使用了这个项目。