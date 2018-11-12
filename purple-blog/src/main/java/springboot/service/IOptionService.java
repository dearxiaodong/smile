package springboot.service;

import springboot.modal.vo.OptionVo;

import java.util.List;
import java.util.Map;

/**
 * @author 357069486@qq.com
 * @date 2018-11-12 13:26
 */
public interface IOptionService {
    void insertOption(OptionVo optionVo);

    void insertOption(String name, String value);

    List<OptionVo> getOptions();


    /**
     * 保存一组配置
     *
     * @param options
     */
    void saveOptions(Map<String, String> options);

    OptionVo getOptionByName(String name);


}
