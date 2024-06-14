//package com.ohx.service;
//
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.ohx.entity.Location;
//
//public interface LocationService extends IService<Location> {
//}

package com.ohx.service;

        import com.baomidou.mybatisplus.extension.service.IService;
        import com.ohx.entity.Location;

        import java.util.List;

public interface LocationService extends IService<Location> {
    List<Location> getLocationByKeyword(String keyword);
}
