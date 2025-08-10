package com.overthinker.cloud.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.overthinker.cloud.common.resp.ResultData;
import com.overthinker.cloud.auditlog.entity.DTO.LogDTO;
import com.overthinker.cloud.web.entity.DTO.LogDeleteDTO;
import com.overthinker.cloud.web.entity.PO.Log;
import com.overthinker.cloud.web.entity.VO.PageVO;


/**
 * (Log)表服务接口
 *
 * @author overH
 * @since 2023-12-12 09:12:32
 */
public interface LogService extends IService<Log> {
    /**
     * 搜索/显示操作日志
     *
     * @param LogDTO  查询条件
     * @param aLong
     * @param current
     * @return 数据列表
     */
    PageVO searchLog(LogDTO LogDTO, Long aLong, Long current);

    /**
     * 删除/清空操作日志
     *
     * @param logDeleteDTO id集合
     * @return 响应结果
     */
    ResultData<Void> deleteLog(LogDeleteDTO logDeleteDTO);

}
