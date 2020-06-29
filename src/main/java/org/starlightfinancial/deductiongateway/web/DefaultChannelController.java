package org.starlightfinancial.deductiongateway.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.DefaultChannel;
import org.starlightfinancial.deductiongateway.service.DefaultChannelService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;
import org.starlightfinancial.deductiongateway.vo.DefaultChannelVO;

import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/7/10 10:42
 * @Modified By:
 */
@Slf4j
@RestController
@RequestMapping("/defaultChannelController")
public class DefaultChannelController {

    @Autowired
    private DefaultChannelService defaultChannelService;


    /**
     * 条件查询记录
     *
     * @param pageBean       分页参数
     * @param defaultChannel 包含了查询条件
     * @return
     */
    @RequestMapping(value = "/queryDefaultChannel.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> queryDefaultChannel(PageBean pageBean, DefaultChannel defaultChannel) {
        PageBean result = defaultChannelService.queryDefaultChannel(pageBean, defaultChannel);
        return Utility.pageBean2Map(result);
    }


    /**
     * 通过银行编码判断是否存在记录
     *
     * @param bankCode 银行编码
     * @return
     */
    @RequestMapping(value = "/isExisted.do")
    public Message isExisted(String bankCode) {
        boolean existed = defaultChannelService.isExisted(bankCode);
        if (existed) {
            return Message.fail("不能进行添加操作,已存在当前银行记录,请直接修改默认启动渠道");
        } else {
            return Message.success();
        }
    }


    /**
     * 保存记录
     *
     * @param defaultChannel
     * @return
     */
    @RequestMapping(value = "/saveDefaultChannel.do")
    public Message saveDefaultChannel(DefaultChannel defaultChannel) {
        Message message;
        try {
            defaultChannelService.saveDefaultChannel(defaultChannel);
            message = Message.success();
        } catch (Exception e) {
            log.debug("保存默认渠道记录失败", e);
            message = Message.fail();
        }
        return message;
    }

    /**
     * 更新记录
     *
     * @param defaultChannelVO
     * @return
     */
    @RequestMapping("/updateDefaultChannel")
    public Message updateDefaultChannel(DefaultChannelVO defaultChannelVO) {
        Message message;
        try {
            DefaultChannel defaultChannel = new DefaultChannel();
            BeanUtils.copyProperties(defaultChannelVO, defaultChannel);
            defaultChannelService.updateDefaultChannel(defaultChannel);
            message = Message.success();
        } catch (Exception e) {
            log.debug("更新默认渠道记录失败", e);
            message = Message.fail();
        }
        return message;
    }
}
