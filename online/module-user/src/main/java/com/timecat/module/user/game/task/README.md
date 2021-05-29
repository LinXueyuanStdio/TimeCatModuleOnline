任务系统设计

# Table
OwnActivity(user, activity, start_time, end_time)
OwnTask(user, activity, task, receive)
Activity(title, content, type, head)
Task(title, content, type, head)
TaskRule(where, targetCount)

# Flow
潜在的触发点
user do what

中心化的任务计算器
user do what -> user finish what tasks

任务完成的提示
user finish what tasks

# Detail
## 潜在的触发点

保存一条完整记录时都可能触发 onSave(Record)
完整记录包括某个表的一条或多条记录

所以在保存时统一都触发即可
触发时发送的消息称为触发消息

## 中心化的任务计算器
加载时机：游戏系统启动时

### 消息过滤器
因为保存某个数据时都会触发任务接收器，很多都和用户当前的任务列表无关。
所以需要过滤出用户正在跟踪的任务，根据这些任务制作触发消息过滤器
每个任务经典的条件是TaskRule，表明某个表的某些统计字段满足什么条件
每个任务包含一个或多个TaskRule，每个TaskRule涉及一张或多张表

Activity (n-n) Task (n-n) TaskRule (n-n) TableTrigger

### 规则校验器

现在过滤后的消息触发了一些规则，需要根据规则查询是否满足这些规则的条件

### 任务进度

现在一些规则满足条件了，触发相关联的任务的进度更新
数值型的任务只需跟踪对应的数值，当前的数值就是进度，数值达到targetCount后则完成
组合型的任务只需跟踪已满足条件个数，当且仅当所有条件都满足时，任务完成

一旦有任务完成，则将对应的任务及其进度提交给任务完成提示器

## 任务完成提示器

现在有一些已经完成的任务，用户决定领取和不领取
已领取任务会被标记，不会再在消息过滤器中考虑

# Implementation
均为云函数，云端保存状态，状态包括了关联图、任务进度，任务完成则推送
## 潜在的触发点
切面，save时统一发送消息，调用任务计算器
## 任务计算器
### 消息过滤器
构建关联图
### 规则校验器
根据一条规则发起查询，返回当前规则进度
### 任务进度
根据规则进度更新任务进度
## 任务完成提示器
领取任务会修改关联图

# 需求

## 用户角度

- 领取任务（主动+被动）
- 做任务
- 收到任务完成的提示（被动）
- 领取任务奖励（主动）

## 策划角度

- 发放任务（主动）







