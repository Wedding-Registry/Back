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
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class ServiceapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceapiApplication.class, args);
	}
}
