管理员

- forum 管理论坛


## 论坛管理

1. 给帖子打标签

## 权限管理

UI id, api id, Route id <--> Meta Permission 元权限

元权限 Meta Permission <--正则匹配-- Hun Permission 混权限 

混权限 Hun Permission <--> Role 角色

默认角色，临时角色 Role <--> Identity 身份（政治化）

身份（政治化） Identity <--> User 用户

Block(user=creator, title=id, content=meta permission) //元权限
Block(user=creator, title=description, content=hun permission) //混权限
Block(user=creator, title=role name, content=description) //角色
Block(user=creator, title=identity name, content=description) //身份

Block2Block(user=creator, from=identity, to=role) //身份有默认角色
Block2Block(user=creator, from=role, to=hun permission) //角色有混权限

InterAction(user=creator, block=permission, target=target user) //授权
InterAction(user=creator, block=role,       target=target user) //授予角色
InterAction(user=creator, block=identity,   target=target user) //授予身份

|---------user-------|
|          |         |
identity  role    permission
|          |
role      permission
|
permission


### 授权

A 授权 B 以 X 身份

1. A 生成 X 身份物品，记录下id
2. B 填写 id，领取 X 身份物品（唯一）
3. B 使用身份物品，获得身份 X 

### 鉴权

1. B 有身份 X1, X2, ... Xn
2. 当前上下文需要权限 Y
3.  X1, X2, ... Xn 是否包含权限 Y

如果有权限id却没有元权限，则加载不成功，谨慎起见，直接拒绝
