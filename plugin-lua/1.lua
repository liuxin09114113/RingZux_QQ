local robot = {};
--主要api
robot.api = {};
--robot.api:SendGroupMessage(MessageFactory factory)   --发送群消息
--robot.api:SendFriendMessage(MessageFactory factory)   --发送好友消息
--次要api
robot.extraapi = {};
--robot.extraapi:reload(robot.name())   --重新载入本插件

--消息传入主入口
function robot.onqqmessage(msg)
    if(msg.Group_uin == 0)
        then
            robot.onfriendmessage(msg)
        else
            robot.ongroupmessage(msg)
    end
end

--处理群消息
function robot.ongroupmessage(msg)
    local factory = luajava.newInstance("com.Tick_Tock.PCTIM.sdk.MessageFactory")
    --MessageFactory.message_type  --int 消息类型 0文本/1xml/2图片
    --MessageFactory.Message  --String 消息内容 文本内容/图片路径/xml内容
    --MessageFactory.Group_uin  --long 目标群号码
    --MessageFactory.Friend_uin  --long 目标好友号码
    factory.Group_uin=msg.Group_uin
    factory.message_type=0;
    if(msg.Message == "测试")
        then
            factory.Message="测试成功"
            robot.api:SendGroupMessage(factory)
    elseif(msg.Message == "重载")
        then
            robot.extraapi:reload(robot.name());
    elseif(msg.Message == "测试http")
        then
            factory.Message=robot.extraapi:gethttpentry("http://119.29.29.29/d?dn=m.baidu.com"):request();
            robot.api:SendGroupMessage(factory)
    elseif(string.match(msg.Message,"测试图片\\s+[0-9]*") ~= nil)
        then
            factory.message_type=2;
            factory.Message="/sdcard/Pictures/Screenshots/"..string.gsub(msg.Message,"测试图片\\s+","")..".png"
            robot.api:SendGroupMessage(factory)
    end
end


--处理好友消息
function robot.onfriendmessage(msg)
    print(msg.Message)
end

--返回插件名字
function robot.name()
    return 'lua测试'
end



--插件加载入口 传入api 已线程化
function robot.load(api,extraapi)
    robot.api=api;
    robot.extraapi=extraapi;
end



return robot