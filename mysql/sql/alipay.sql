

DROP TABLE IF EXISTS `alipay_payment`;

CREATE TABLE `alipay_payment`(
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `appid` VARCHAR (32) COMMENT 'appid',
                                 `receiver_account` VARCHAR (16) COMMENT 'receiverAccount',
                                 `buyer_logon_id` VARCHAR (16) COMMENT 'buyerLogonId',
                                 `trade_no` VARCHAR (64) COMMENT 'tradeNo',
                                 `out_trade_no` VARCHAR (64) COMMENT 'outTradeNo',
                                 `trade_status` INT COMMENT 'tradeStatus',
                                 `total_amount` INT (11) COMMENT 'tradeAmount',
                                 `success_time` DATETIME COMMENT 'successTime',
                                 PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'alipay_payment';


DROP TABLE IF EXISTS `alipay_div_receiver`;

CREATE TABLE `alipay_div_receiver`(
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `out_request_no` VARCHAR (32) COMMENT 'outRequestNo',
                                      `trans_out` VARCHAR (16) COMMENT 'transOut',
                                      `type` VARCHAR (32) COMMENT 'type',
                                      `account` VARCHAR (16) COMMENT 'account',
                                      `bind_login_name` VARCHAR (150) COMMENT 'bindLoginName',
                                      PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'alipay_div_receiver';

DROP TABLE IF EXISTS `alipay_settlement`;

CREATE TABLE `alipay_settlement`(
                                    `id` bigint NOT NULL AUTO_INCREMENT,
                                    `settle_no` VARCHAR (64) COMMENT 'settleNo',
                                    `out_request_no` VARCHAR (32) COMMENT 'outRequestNo',
                                    `trade_no` VARCHAR (64) COMMENT 'tradeNo',
                                    `out_trade_no` VARCHAR (64) COMMENT 'outTradeNo',
                                    `amount_total` Integer COMMENT 'amount_total',
                                    `success_time` DATETIME COMMENT 'successTime',
                                    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'alipay_settlement';





DROP TABLE IF EXISTS `alipay_royalty_detail`;

CREATE TABLE `alipay_royalty_detail`(
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `trans_in` VARCHAR (16) COMMENT 'transIn',
                                        `trans_out` VARCHAR (16) COMMENT 'transOut',
                                        `amount` Integer COMMENT 'amount',
                                        `settle_no` VARCHAR (64) COMMENT 'settleNo',
                                        PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'alipay_royalty_detail';





DROP TABLE IF EXISTS `alipay_refund`;

CREATE TABLE `alipay_refund`(
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `out_request_no` VARCHAR (32) COMMENT 'outRequestNo',
                                `out_trade_no` VARCHAR (64) COMMENT 'outTradeNo',
                                `trade_no` VARCHAR (64) COMMENT 'tradeNo',
                                `total_amount` Integer,
                                `refund_amount` Integer,
                                `gmt_refund_pay` DATETIME COMMENT 'gmt_refund_pay',
                                `refund_status` INT COMMENT 'refundStatus',
                                PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'alipay_refund';


DROP TABLE IF EXISTS `alipay_refund_royalty_detail`;

CREATE TABLE `alipay_refund_royalty_detail`(
                                               `id` bigint NOT NULL AUTO_INCREMENT,
                                               `out_request_no` VARCHAR (32) COMMENT 'outRequestNo',
                                               `out_trade_no` VARCHAR (64) COMMENT 'outTradeNo',
                                               `trade_no` VARCHAR (64) COMMENT 'tradeNo',
                                               `trans_in` VARCHAR (16) COMMENT 'transIn',
                                               `trans_out` VARCHAR (16) COMMENT 'transOut',
                                               `refund_amount` Integer COMMENT 'refund_amount',
                                               `royalty_type` VARCHAR (16) COMMENT 'royalty_type',
                                               `result_code` VARCHAR (16) COMMENT 'result_code',
                                               `gmt_refund_pay` DATETIME COMMENT 'gmt_refund_pay',
                                               PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'alipay_refund_royalty_detail';








