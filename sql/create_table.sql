# 数据库初始化
# @author lts
#

-- 创建库
create database if not exists steadyheartapi;

-- 切换库
use steadyheartapi;

-- 接口信息
create table if not exists steadyheartapi.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '接口名',
    `description` varchar(256) null comment '接口信息描述',
    `url` varchar(512) not null comment '接口地址',
    `requestParams` text not null comment '请求参数',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人id',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '接口信息';

insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('邱鹏煊', 'tkVC', 'www.ben-jast.net', 'xa', 'M2', 'LTTZR', '5Ag', 79500088);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('林志泽', 't4', 'www.bud-howe.com', 'aF', '4p8', 'Gh1i', 'xzL', 40929);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('田炎彬', 'Wnr6k', 'www.deon-schamberger.co', '7Wnk', '8f', 'q5', 'p1G', 42);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('梁伟祺', 'bQh9g', 'www.lemuel-sawayn.com', 'Dr', 'jHz', 'AVP', 'Km', 89);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('邹烨霖', '2kA', 'www.erwin-skiles.biz', 'OHlhw', 'lL', 'v9eDf', 'E0L3', 5557033);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('唐驰', 'ichz', 'www.lonnie-gorczany.org', 'hgi0', 'vwv', 'Bj', 'h8K', 1635095);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('熊笑愚', 'vm4', 'www.maria-ankunding.org', '4nTnr', 'GGbeH', '4rr', '9fm', 735);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('陆擎苍', 'A4F', 'www.elenora-walter.biz', 'mM', 'OaeO', 'LH', 'vUEQy', 97);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('徐文博', 'xLPlk', 'www.sharell-armstrong.co', 'dl0', 'OIiYr', 'q2dsP', 'EH', 1446583);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('黎语堂', '6fg', 'www.aurora-bednar.co', '1B2', 'a4', 'xr', 'SR99Z', 3053);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('金鹤轩', 'p4f', 'www.benny-nitzsche.biz', '3C8OC', 'zZP', 'fvs', 'EqX', 125252);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('贺潇然', 'c4J', 'www.sonja-bayer.com', 'iVT', 'qt', 'c59J', 'fnO3m', 7);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('汪智辉', 'qhrRf', 'www.gino-blanda.org', 'Ee', 's5My', '1yaae', '997DW', 6);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('姚立轩', 'GbVX', 'www.beverley-wehner.org', 'jh7', 'QPOoZ', '4wCfb', 'xH', 73934);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('余金鑫', 'dAT', 'www.douglas-boehm.net', 'kL', 'SRH', '731', 'G1', 160715929);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('吕泽洋', 'hka', 'www.darcie-jenkins.org', 'UpndU', '87ct', '9oLw5', '2S', 17664);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('毛立轩', 'Fy5', 'www.willian-conroy.biz', '8KCn', 'IkY', 'f8O', 'le', 3663059144);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('洪志强', 'nybQ', 'www.weston-carter.name', '17VH', '5c', 'Xd', '5QbSX', 6257593);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('蔡明杰', 'ySZ4', 'www.cathleen-sauer.name', 'jdk', 'WDU', 'BErwC', 'D3h', 7);
insert into steadyheartapi.`interface_info` (`name`, `description`, `url`, `requestParams`, `requestHeader`, `responseHeader`, `method`, `userId`) values ('万博文', 'aoSET', 'www.ewa-turcotte.info', 'UOMH7', 'aWP', 'Pw4k', 'Pd', 435);
