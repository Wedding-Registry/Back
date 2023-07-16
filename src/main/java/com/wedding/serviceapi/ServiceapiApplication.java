package com.wedding.serviceapi;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.goods.domain.Commerce;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.repository.GoodsRepository;
import com.wedding.serviceapi.goods.repository.UsersGoodsRepository;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
public class ServiceapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceapiApplication.class, args);
	}


//	@Autowired
//	private UsersRepository usersRepository;
//	@Autowired
//	private BoardsRepository boardsRepository;
//	@Autowired
//	private GoodsRepository goodsRepository;
//	@Autowired
//	private UsersGoodsRepository usersGoodsRepository;
//
//	@PostConstruct
//	public void setting() {
//		Goods goods1 = new Goods("imgUrl1", "goodsUrl1", "goods1", 100000, Commerce.NAVER);
//		Goods goods2 = new Goods("imgUrl2", "goodsUrl2", "goods2", 200000, Commerce.NAVER);
//		Users users = usersRepository.findById(1L).get();
//		Boards boards = boardsRepository.findById(1L).get();
//
//		goodsRepository.save(goods1);
//		goodsRepository.save(goods2);
//
//		UsersGoods usersGoods1 = UsersGoods.builder().users(users).goods(goods1).boards(boards).updatedUsersGoodsName(goods1.getGoodsName()).updatedUsersGoodsPrice(goods1.getGoodsPrice()).wishGoods(true).build();
//		UsersGoods usersGoods2 = UsersGoods.builder().users(users).goods(goods2).boards(boards).updatedUsersGoodsName(goods2.getGoodsName()).updatedUsersGoodsPrice(goods2.getGoodsPrice()).wishGoods(true).build();
//		UsersGoods usersGoods3 = UsersGoods.builder().users(users).goods(goods1).boards(boards).updatedUsersGoodsName(goods1.getGoodsName()).updatedUsersGoodsPrice(goods1.getGoodsPrice()).wishGoods(true).build();
//		UsersGoods usersGoods4 = UsersGoods.builder().users(users).goods(goods2).boards(boards).updatedUsersGoodsName(goods2.getGoodsName()).updatedUsersGoodsPrice(goods2.getGoodsPrice()).wishGoods(true).build();
//		UsersGoods usersGoods5 = UsersGoods.builder().users(users).goods(goods1).boards(boards).updatedUsersGoodsName(goods1.getGoodsName()).updatedUsersGoodsPrice(goods1.getGoodsPrice()).wishGoods(true).build();
//		UsersGoods usersGoods6 = UsersGoods.builder().users(users).goods(goods2).boards(boards).updatedUsersGoodsName(goods2.getGoodsName()).updatedUsersGoodsPrice(goods2.getGoodsPrice()).wishGoods(true).build();
//		UsersGoods usersGoods7 = UsersGoods.builder().users(users).goods(goods1).boards(boards).updatedUsersGoodsName(goods1.getGoodsName()).updatedUsersGoodsPrice(goods1.getGoodsPrice()).wishGoods(true).build();
//		UsersGoods usersGoods8 = UsersGoods.builder().users(users).goods(goods2).boards(boards).updatedUsersGoodsName(goods2.getGoodsName()).updatedUsersGoodsPrice(goods2.getGoodsPrice()).wishGoods(true).build();
//		usersGoodsRepository.saveAllAndFlush(List.of(usersGoods1, usersGoods2, usersGoods3, usersGoods4, usersGoods5, usersGoods6, usersGoods7, usersGoods8));
//	}


}
