package serviceimpl;

import com.baomidou.dynamic.datasource.annotation.DS;
import dao.CtPigDemandapplicationDao;
import dao.CtPigSpermallotrecordDao;
import entity.CtPigDemandapplication;
import entity.CtPigSpermallotrecord;
import entity.TPmUser;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.PigDemandService;
import service.SpermRecordService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author AIERXUAN
 */
@Service
@Slf4j
@DS("slave1")
public class SpermRecordServiceImpl implements SpermRecordService {
    private final PigDemandService pigDemandService;
    private final CtPigSpermallotrecordDao ctPigSpermallotrecordDao;
    private final CtPigDemandapplicationDao ctPigDemandapplicationDao;
    
    @Autowired
    public SpermRecordServiceImpl(PigDemandService pigDemandService, CtPigSpermallotrecordDao ctPigSpermallotrecordDao, CtPigDemandapplicationDao ctPigDemandapplicationDao) {
        this.pigDemandService = pigDemandService;
        this.ctPigSpermallotrecordDao = ctPigSpermallotrecordDao;
        this.ctPigDemandapplicationDao = ctPigDemandapplicationDao;
    }

    @Override
    public List<CtPigDemandapplication> selectDemand(String orgId) {
        List<CtPigDemandapplication> list = pigDemandService.selectDeamands(orgId,"kvYAABM5rTrbYNTG");
        List<CtPigDemandapplication> list2 = pigDemandService.selectDeamands(orgId,"kvYAABM5rTnbYNTG");
        //两个集合合并去重
        list.removeAll(list2);
        list.addAll(list2);
        return list;
    }

    @Override
    public List<CtPigDemandapplication> selectAllDemand(String orgId) {
        List<CtPigDemandapplication> list = pigDemandService.selectAllDeamands(orgId,"kvYAABM5rTrbYNTG");
        List<CtPigDemandapplication> list2 = pigDemandService.selectAllDeamands(orgId,"kvYAABM5rTnbYNTG");
        //两个集合合并去重
        list.removeAll(list2);
        list.addAll(list2);
        return list;
    }

    @Override
    public int insertSpermRecord(CtPigSpermallotrecord ctPigSpermallotrecord) {
        return ctPigSpermallotrecordDao.insert(ctPigSpermallotrecord);
    }

    @Override
    public int deleteSpermRecord(String fid) {
        return ctPigSpermallotrecordDao.deleteByPrimaryKey(fid);
    }

    @Override
    public int updateSpermRecord(CtPigSpermallotrecord ctPigSpermallotrecord) {
        return ctPigSpermallotrecordDao.updateByPrimaryKey(ctPigSpermallotrecord);
    }

    @Override
    public List<CtPigSpermallotrecord> selectSpermallotrecord(String orgId,Date start, Date end) {
        return ctPigSpermallotrecordDao.selectAllByFbizdates(orgId, start, end);
    }

    @Override
    public List<CtPigSpermallotrecord> selectPuzzy(String orgId,String name) {
        return ctPigSpermallotrecordDao.selectAll(orgId, name);
    }

    @Override
    public List<Integer> audit(@NotNull List<CtPigSpermallotrecord> ctPigSpermallotrecords, @NotNull TPmUser tPmUser) {
        List<Integer> result = new ArrayList<>();
        String userId = tPmUser.getFid();
        for (CtPigSpermallotrecord ctPigSpermallotrecord : ctPigSpermallotrecords) {
            String fid = ctPigSpermallotrecord.getFid();
            //更新需求单的状态字段
            //1.获取需求单fid和完成状态fid
            String applicationFid = ctPigSpermallotrecord.getCfapplicationid();
            String completeId = ctPigSpermallotrecord.getCfcompletestatusid();
            CtPigDemandapplication ctPigDemandapplication = ctPigDemandapplicationDao.selectByPrimaryKey(applicationFid);
            String cfapplicationstatu = ctPigDemandapplication.getCfapplicationstatu();
            //修改需求申请单完成状态
            ctPigDemandapplicationDao.updateByPrimaryKey(applicationFid,cfapplicationstatu,completeId);
            result.add(ctPigSpermallotrecordDao.updateIfAuditByFid(fid, userId));
        }
        return result;
    }


}
