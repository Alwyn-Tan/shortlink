package org.alwyn.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.alwyn.shortlink.admin.dao.entity.GroupDO;
import org.alwyn.shortlink.admin.dto.req.GroupSortReqDTO;
import org.alwyn.shortlink.admin.dto.req.GroupUpdateReqDTO;
import org.alwyn.shortlink.admin.dto.resp.GroupListQueryRespDTO;

import java.util.List;

public interface GroupService extends IService<GroupDO> {
    void createGroupByGroupName(String groupName);

    void createGroupByGroupName(String groupName, String username);

    List<GroupListQueryRespDTO> listGroup();

    Integer updateGroupByGroupName(GroupUpdateReqDTO requestParam);

    Integer deleteGroupByGid(String gid);

    void sortGroup(List<GroupSortReqDTO> requestParam);
}
