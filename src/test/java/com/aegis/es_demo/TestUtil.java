package com.aegis.es_demo;

import com.aegis.es_demo.dao.GirlFriendDao;
import com.aegis.es_demo.domin.GirlFriend;
import com.aegis.es_demo.service.impl.MethodService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import com.aegis.es_demo.domin.Properties;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestUtil {

//    @Test
//    public void testCreatToken(){
//        String username = "李刚";
//        String password = "123456";
//        String token = JWTUtil.sign(username,password);
//        System.out.println((token));
////        返回数据
////        eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9. 这一段是header，这一部分包含算法类型和token类型
////        eyJleHAiOjE1OTE2OTU0NTMsInVzZXJuYW1lIjoibWlhb3RlbmcifQ. 这一段是payload 负载 存放数据
////        HKeMOqoHdU57YKZv5D7Q0wnsA_kAEgRLvAulHhJbcuLmRtv1zaofZ 这一段是signature 签名 对前两部分的签名
////        密钥很重要
//        DecodedJWT jwt = JWT.decode(token);
//        System.out.println(jwt.getClaim("username").asString());
//        User user = JWTUtil.getUsername(token);
//        System.out.println(user);
//
//        System.out.println(JWTUtil.verify(token,"李刚","123456"));
//
//    }
//
//    @Test
//    public void testToken(){
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9." +
//                "eyJleHAiOjE1OTE2OTgwODEsInVzZXJuYW1lIjoi5p2O5YiaIn0." +
//                "KbsVpbHTHTGqttFJFOUwNbbUMcj9bBt6MZEVQ3UqMRyHcinmxXpo7HEAxE9W9wjogA6PKxtkukpeNVesg2c9dw";
//
//        DecodedJWT jwt = JWT.decode(token);
//        String username = jwt.getClaim("username").asString();
//        System.out.println(JWTUtil.getUsername(username));
//    }
//
//    @Test
//    public void testRegister(){
//
//    }
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @Test
//    public void testRedis(){
////        Map entries = redisTemplate.opsForHash().entries("dispute:city");
//        Set keys = redisTemplate.opsForHash().keys("dispute:city");
//        keys.forEach(k-> System.out.println(k));
////        entries.keySet().forEach(key-> System.out.println(key));
////        System.out.println(entries.keySet().contains("001"));
//
//    }
//    @Test
//    public void tToken() {
//
//    }
////    @Autowired
////    Properties properties;
////    @Test
////    public void testPro() {
////        System.out.println(properties.getFirstName());
////        System.out.println(properties.getMiddleName());
////    }
//
//    @Test
//    public void uploadFile() throws IOException {
//        String filePath = "/Users/miteng/Desktop/改大连市人民调解机构.xls";
//
//        FileInputStream fileInputStream = new FileInputStream(filePath);
//        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
//        POIFSFileSystem fileSystem = new POIFSFileSystem(bufferedInputStream);
//        HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
//        HSSFSheet sheet = workbook.getSheet("Sheet1");
//
//        int lastRowIndex = sheet.getLastRowNum();
//        System.out.println(lastRowIndex);
//    }
//
//
//
//    public static void readExcel() throws IOException {
//        String filePath = "/Users/miteng/Desktop/改大连市人民调解机构.xls";
//
//        FileInputStream fileInputStream = new FileInputStream(filePath);
//        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
//        POIFSFileSystem fileSystem = new POIFSFileSystem(bufferedInputStream);
//        HSSFWorkbook workbook = new HSSFWorkbook(fileSystem);
//        HSSFSheet sheet = workbook.getSheet("Sheet1");
//
//        int lastRowIndex = sheet.getLastRowNum();
////        System.out.println(lastRowIndex);
////        for (int i = 0; i <= lastRowIndex; i++) {
////            HSSFRow row = sheet.getRow(i);
////            if (row == null) {
////                break;
////            }
////
////            short lastCellNum = row.getLastCellNum();
////            for (int j = 0; j < lastCellNum; j++) {
////                String cellValue = row.getCell(j).getStringCellValue();
////                System.out.println(cellValue);
////            }
////        }
//    }
//    @Autowired
//    private RedisUtil redisUtil;
//    @Test
//    public void testHash() {
//        Map<String,String> map = new HashMap<>();
//        map.put("001","sdf");
//        map.put("002","asda");
//        Set<Map.Entry<String, String>> entries = map.entrySet();
//        Map<Integer,String> newMap = new HashMap<>();
//        newMap.put(1,"hello");
//        newMap.put(2,"world");
//        redisUtil.hset("dispute:city","001",newMap);
//        redisUtil.hset("dispute:city","002","c scasd");
//    }
//    @Test
//    public void testGet() {
////        Map executeResult = (Map) redisTemplate.executePipelined(new RedisCallback<Map>() {
////            @Nullable
////            @Override
////            public Map doInRedis(RedisConnection connection) throws DataAccessException {
////                connection.hGetAll("dispute:city".getBytes());
////                return null;
////            }
////        });
////        System.out.println(executeResult);
//        String year = 2020+"-01-01";
//        System.out.println(year+1+"fsdfsd");
//    }
    @Autowired
    GirlFriendDao girlFriendDao;
    @Autowired
    MethodService methodService;
    @Test
    public void update() {
        methodService.method1();
    }

    @Test
    public void sss() {
        GirlFriend girlFriend = new GirlFriend();
        girlFriend.setName("aaaa");
        girlFriendDao.save(girlFriend);
    }
//
//    @Test
//    public void save() {
//        GirlFriend girlFriend = new GirlFriend();
//        girlFriend.setId("3");
//        girlFriend.setState(5);
//        girlFriendDao.save(girlFriend);
//    }
//
//    @Autowired
//    EntityManager entityManager;
//    @Test
//    public void sssss() {
////        String sql = "update user set nick_name='康康' where id=17";
//        String sql = "select * from user where id = 17";
//        Query nativeQuery = entityManager.createNativeQuery(sql, User.class);
//        User user = (User) nativeQuery.getSingleResult();
//        System.out.println(user);
//        user.setNickName("康康");
//        entityManager.merge(user);
//        entityManager.flush();
//
//    }
//    @Autowired
//    UserDao userDao;
//    @Test
//    public void updateUser() {
//        User user = userDao.findById(17).get();
//        user.setNickName("康康");
//        userDao.save(user);
//    }

}
