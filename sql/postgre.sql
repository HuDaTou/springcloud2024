/*
 Navicat Premium Data Transfer

 Source Server         : home
 Source Server Type    : MySQL
 Source Server Version : 80401 (8.4.1)
 Source Host           : home.overthinker.top:30970
 Source Schema         : easybilibili

 Target Server Type    : PostgreSQL
 Target Server Version : 16.x
 File Encoding         : 65001

 Date: 25/02/2025 02:10:16
*/

SET client_encoding = 'UTF8';


-- ----------------------------
-- Table structure for sys_website_info
-- ----------------------------
DROP TABLE IF EXISTS sys_website_info CASCADE;
CREATE TABLE sys_website_info (
  id bigserial NOT NULL,
  webmaster_avatar varchar(100) DEFAULT NULL,
  webmaster_name varchar(50) DEFAULT NULL,
  webmaster_copy varchar(100) DEFAULT NULL,
  webmaster_profile_background varchar(255) DEFAULT NULL,
  gitee_link varchar(100) DEFAULT NULL,
  github_link varchar(100) DEFAULT NULL,
  website_name varchar(50) DEFAULT NULL,
  header_notification varchar(150) DEFAULT NULL,
  sidebar_announcement varchar(255) DEFAULT NULL,
  record_info varchar(255) DEFAULT NULL,
  start_time timestamp DEFAULT NULL,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

COMMENT ON TABLE sys_website_info IS '网站信息表';
COMMENT ON COLUMN sys_website_info.webmaster_avatar IS '站长头像';
COMMENT ON COLUMN sys_website_info.webmaster_name IS '站长名称';
COMMENT ON COLUMN sys_website_info.webmaster_copy IS '站长文案';
COMMENT ON COLUMN sys_website_info.webmaster_profile_background IS '站长资料卡背景图';
COMMENT ON COLUMN sys_website_info.gitee_link IS 'gitee链接';
COMMENT ON COLUMN sys_website_info.github_link IS 'github链接';
COMMENT ON COLUMN sys_website_info.website_name IS '网站名称';
COMMENT ON COLUMN sys_website_info.header_notification IS '头部通知';
COMMENT ON COLUMN sys_website_info.sidebar_announcement IS '侧面公告';
COMMENT ON COLUMN sys_website_info.record_info IS '备案信息';
COMMENT ON COLUMN sys_website_info.start_time IS '开始运行时间';
COMMENT ON COLUMN sys_website_info.create_time IS '用户创建时间';
COMMENT ON COLUMN sys_website_info.update_time IS '用户更新时间';
COMMENT ON COLUMN sys_website_info.is_deleted IS '是否删除（false：未删除，true：已删除）';

-- ----------------------------
-- Records of sys_website_info
-- ----------------------------
INSERT INTO sys_website_info VALUES (
  1,
  E'http://minioapi.overthinker.top/cloud/websiteInfo/avatar/a233c455-f1d8-4058-81e8-a8642c9fc25f.jpg',
  E'overthinker',
  E'过度思考未来，无异于杀死现在的自己',
  E'http://minioapi.overthinker.top/cloud/websiteInfo/background/4b55d71d-45ce-4dd4-a51f-30e8cd23fadc.png',
  E'https://gitee.com/',
  E'https://github.com/',
  E'overthinker',
  E'欢迎来到overthinker',
  E'欢迎指出网站的不足，给我提供意见',
  E'备案信息',
  '2024-01-01 16:00:25',
  '2023-12-27 14:28:10',
  '2025-02-24 03:11:55',
  false
);
SELECT setval('sys_website_info_id_seq', 10);


-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS t_article CASCADE;
CREATE TABLE t_article (
  id bigserial NOT NULL,
  user_id bigint NOT NULL,
  category_id bigint NOT NULL,
  article_cover varchar(1024) NOT NULL,
  article_title varchar(50) NOT NULL,
  article_content text NOT NULL,
  article_type smallint NOT NULL,
  is_top boolean NOT NULL,
  status smallint NOT NULL,
  visit_count bigint NOT NULL DEFAULT 0,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_article IS '文章表';
COMMENT ON COLUMN t_article.id IS '文章id';
COMMENT ON COLUMN t_article.user_id IS '作者id';
COMMENT ON COLUMN t_article.category_id IS '分类id';
COMMENT ON COLUMN t_article.article_cover IS '文章缩略图';
COMMENT ON COLUMN t_article.article_title IS '文章标题';
COMMENT ON COLUMN t_article.article_content IS '文章内容';
COMMENT ON COLUMN t_article.article_type IS '类型 (1原创 2转载 3翻译)';
COMMENT ON COLUMN t_article.is_top IS '是否置顶 (false否 true是）';
COMMENT ON COLUMN t_article.status IS '文章状态 (1公开 2私密 3草稿)';
COMMENT ON COLUMN t_article.visit_count IS '访问量';
COMMENT ON COLUMN t_article.create_time IS '文章创建时间';
COMMENT ON COLUMN t_article.update_time IS '文章更新时间';
COMMENT ON COLUMN t_article.is_deleted IS '是否删除（false：未删除，true：已删除）';

-- ----------------------------
-- Records of t_article
-- ----------------------------
INSERT INTO t_article VALUES (3, 22, 1, E'https://www.baidu.com', E'测试文章', E'测试文章内容', 1, false, 1, 0, '2025-02-24 21:30:54', '2025-02-25 02:08:02', false);

INSERT INTO t_article VALUES (41, 1, 13, E'http://minioapi.overthinker.top/cloud/article/articleCover/41455699-438b-4e36-aa3f-eec094383386.png', E'你好', E'# 2025 年 2 月 23 日：生活的小确幸\n\n清晨，柔和的阳光透过轻薄的窗帘，小心翼翼地洒进房间，温柔地唤醒了我。我慵懒地伸了个懒腰，新的一天就这样拉开了序幕。\n\n简单地洗漱完毕，我来到厨房，准备给自己做一顿丰盛的早餐。窗外的鸟儿欢快地唱着歌，仿佛在为这美好的一天欢呼。我煎了个金黄酥脆的鸡蛋，烤了两片香气四溢的面包，再配上一杯热气腾腾的咖啡，坐在餐桌前，细细品味着这份属于自己的宁静与满足。\n\n早餐过后，我决定出门去附近的公园散步。公园里的景色美不胜收，嫩绿的新芽在枝头探头探脑，五彩斑斓的花朵竞相绽放，散发出阵阵迷人的芬芳。我沿着蜿蜒的小径漫步，感受着微风轻拂脸颊的惬意，聆听着树叶沙沙作响的声音，仿佛与大自然融为一体。\n\n在公园里，我遇到了一些可爱的小朋友，他们在草地上嬉笑玩耍，那纯真无邪的笑容如同阳光一般灿烂。我忍不住停下脚步，看着他们尽情地享受着童年的快乐，心中也充满了温暖和喜悦。\n\n中午时分，我回到家中，简单地吃了些午餐后，便开始了自己的学习时光。我坐在书桌前，翻开一本自己喜欢的书，沉浸在知识的海洋中。时间在不知不觉中流逝，当我合上书的那一刻，仿佛经历了一场奇妙的旅行，收获满满。\n\n傍晚，我和家人一起去了一家温馨的餐厅吃晚餐。餐厅里弥漫着浓郁的美食香气，我们一边品尝着美味的菜肴，一边分享着一天中的趣事。欢声笑语回荡在餐厅里，让我感受到了家庭的温暖和幸福。\n\n晚饭后，我们沿着街道散步，欣赏着城市夜晚的美景。路灯下，人影绰绰，车辆川流不息，整个城市都散发着一种独特的魅力。我们谈论着生活中的点点滴滴，感受着这份平凡而又珍贵的时光。\n\n回到家中，我坐在窗前，望着窗外闪烁的星星，回顾着这一天的生活。虽然没有什么惊天动地的大事发生，但每一个瞬间都充满了温暖和快乐。生活或许就是这样，平淡而又真实，只要我们用心去感受，就能发现其中的小确幸。\n\n这就是 2025 年 2 月 23 日，一个平凡而又美好的日子。我期待着明天会有更多的惊喜和感动。 ', 1, false, 1, 0, '2025-02-23 18:41:35', '2025-02-23 18:41:35', false);

INSERT INTO t_article VALUES (42, 1, 13, E'http://minioapi.overthinker.top/cloud/article/articleCover/530d1a23-a1b7-4117-98b2-a8a8c4f28293.png', E'一场充满惊喜与挑战的旅程', E'\n生活，宛如一幅绚丽多彩的画卷，每个人都是这画卷的创作者，用自己的经历、情感和梦想，描绘出独一无二的图案。它既有阳光明媚的日子，也会有风雨交加的时刻，正是这些不同的色彩和笔触，构成了生活的丰富与深邃。\n\n## 平凡日子里的小确幸\n清晨，阳光透过窗帘的缝隙，温柔地洒在脸上，带来新一天的问候。走进厨房，煮上一杯香浓的咖啡，听着咖啡豆研磨的声音，仿佛是生活奏响的美妙序曲。伴随着咖啡的香气，翻开一本心仪已久的书籍，沉浸在文字的世界里，与作者进行一场跨越时空的对话。此时，时光仿佛慢了下来，每一个字都像是一颗璀璨的珍珠，串联起内心的宁静与满足。\n\n午后，漫步在公园的小径上，微风轻拂，树叶沙沙作响，似在诉说着大自然的故事。路边的花朵竞相绽放，红的像火，粉的像霞，白的像雪，它们在风中摇曳生姿，散发着迷人的芬芳。偶尔停下脚步，观察一只小蚂蚁忙碌的身影，看它如何搬运食物，如何与同伴交流协作，在这微小的生命中，感受到生命的顽强与坚韧。这样的午后，没有匆忙的脚步，没有繁重的工作，只有与自然的亲密接触，让人忘却一切烦恼，心中充满了对生活的热爱。\n\n夜晚，结束了一天的奔波，回到温馨的家中。与家人围坐在餐桌前，分享着一天的见闻和喜怒哀乐。餐桌上摆满了热气腾腾的饭菜，每一道菜都蕴含着家人的关爱与用心。灯光柔和地洒在每个人的脸上，欢声笑语回荡在房间里，这一刻，亲情的温暖如同一股暖流，流淌在心中，让人感受到生活的美好与幸福。\n\n## 面对挑战时的勇气与成长\n生活并非总是一帆风顺，它也会给我们带来各种各样的挑战。工作上的压力、人际关系的困扰、梦想与现实的差距，都可能让我们感到疲惫和迷茫。然而，正是这些挑战，如同磨刀石一般，磨砺着我们的意志，让我们不断成长和进步。\n\n曾经，我在工作中面临着一个巨大的项目，时间紧迫，任务艰巨，压力让我几乎喘不过气来。无数次，我在深夜里对着电脑屏幕，思考着项目的方案和细节，焦虑和不安如影随形。但是，我没有选择逃避，而是鼓起勇气，一步一个脚印地去解决问题。我主动向同事请教，查阅大量的资料，不断尝试新的方法和思路。在这个过程中，我遇到了许多困难和挫折，但每一次克服困难，都让我感受到自己的成长和进步。最终，项目顺利完成，那一刻，我心中充满了成就感，也明白了只要有勇气面对挑战，就没有什么能够阻挡我们前进的步伐。\n\n在人际关系中，我们也难免会遇到矛盾和冲突。有时候，我们会因为误解而与朋友产生隔阂，或者因为观点不同而与家人发生争执。这些经历可能会让我们感到伤心和难过，但同时也是我们学习如何理解他人、如何沟通和解决问题的宝贵机会。通过真诚地沟通和换位思考，我们往往能够化解矛盾，修复关系，让彼此的感情更加深厚。在这个过程中，我们学会了包容、学会了关爱，也更加懂得珍惜身边的人。\n\n## 追寻梦想，点亮生活的光芒\n生活中，梦想是一束照亮前行道路的光，它让我们的生活充满了希望和动力。每个人都有自己的梦想，或许是成为一名优秀的艺术家，用画笔描绘出心中的美好世界；或许是成为一名救死扶伤的医生，帮助患者战胜疾病，重获健康；或许是成为一名旅行家，踏遍世界各地，领略不同的风土人情。无论梦想是什么，它都承载着我们对生活的热爱和向往。\n\n为了实现梦想，我们需要付出努力和汗水。这意味着要在无数个日日夜夜中坚持学习，不断提升自己的能力；要在面对困难和挫折时，不轻易放弃，始终保持坚定的信念。在追寻梦想的道路上，我们会遇到各种各样的诱惑和干扰，但只要心中有梦想，我们就能够抵御这些诱惑，坚定地朝着目标前进。当我们通过自己的努力，离梦想越来越近时，那种喜悦和满足是无法用言语来形容的。梦想不仅让我们实现了自我价值，也让我们的生活变得更加充实和有意义。\n\n生活，就是这样一个充满惊喜与挑战的旅程。它既有平凡日子里的小确幸，让我们感受到生活的温暖与美好；也有面对挑战时的艰辛与成长，让我们变得更加坚强和成熟；更有追寻梦想的激情与动力，让我们的生活绽放出耀眼的光芒。在这趟旅程中，我们要用心去感受每一个瞬间，用爱去拥抱生活的每一面，因为正是这些丰富多彩的经历，构成了我们独一无二的人生。让我们怀揣着对生活的热爱，勇敢地踏上这趟奇妙的旅程，去书写属于自己的精彩篇章。 ', 1, true, 1, 0, '2025-02-24 02:02:50', '2025-02-24 02:02:50', false);

INSERT INTO t_article VALUES (43, 1, 14, E'http://minioapi.overthinker.top/cloud/article/articleCover/1ea84532-cdb1-4b84-8c05-a523d184aee6.png', E'探索未知，塑造未来', E'当然，我可以为你生成一篇随机的文章。以下是一篇关于“未来科技发展趋势”的示例文章：\n\n---\n\n**未来科技发展趋势：探索未知，塑造未来**\n\n在21世纪的今天，科技正以前所未有的速度改变着我们的生活。从智能手机到无人驾驶汽车，从人工智能到量子计算，科技的每一次进步都在重塑我们的世界。那么，未来的科技发展又将走向何方？本文将探讨几个关键领域，展望未来的科技发展趋势。\n\n**一、人工智能：从辅助到主导**\n\n人工智能（AI）无疑是当前科技领域的热门话题。随着深度学习、自然语言处理等技术的不断进步，AI正在逐渐从辅助工具转变为主导力量。在未来，我们可以预见AI将在更多领域发挥重要作用。例如，在医疗领域，AI将能够辅助医生进行更精准的诊断和治疗；在教育领域，AI将为学生提供个性化的学习方案，提高学习效率。此外，AI还将推动自动驾驶技术的进一步发展，使交通更加安全、高效。\n\n**二、量子计算：突破传统限制**\n\n量子计算是近年来备受瞩目的新兴技术。与传统计算机相比，量子计算机具有更高的计算速度和更强的处理能力。随着量子计算技术的不断成熟，它将在材料科学、药物研发、金融分析等领域发挥巨大作用。例如，在药物研发中，量子计算可以模拟复杂的分子结构，加速新药的发现；在金融分析中，量子计算可以处理大量数据，提高投资决策的准确性。\n\n**三、区块链：重塑信任机制**\n\n区块链技术以其去中心化、不可篡改的特点，正在逐渐改变我们对信任的理解。在未来，区块链将在金融、物流、版权保护等领域发挥重要作用。例如，在金融领域，区块链可以降低交易成本，提高交易效率；在物流领域，区块链可以确保货物的真实性和安全性；在版权保护方面，区块链可以确保创作者的知识产权得到充分保护。\n\n**四、生物技术：探索生命奥秘**\n\n生物技术是近年来发展迅速的领域之一。随着基因编辑、合成生物学等技术的不断进步，我们有望在未来实现更多关于生命的突破。例如，在医疗领域，基因编辑技术可以用于治疗遗传性疾病；在农业领域，合成生物学可以培育出更耐旱、抗病虫害的作物。此外，生物技术还将推动人类寿命的延长和生命质量的提升。\n\n**五、可持续发展技术：应对全球挑战**\n\n面对全球气候变化和资源短缺的挑战，可持续发展技术成为未来科技发展的重要方向。例如，在能源领域，可再生能源技术将逐渐取代化石燃料，成为主要的能源来源；在环保领域，废物回收和再利用技术将减少资源浪费和环境污染。此外，智能城市、绿色建筑等概念也将推动城市的可持续发展。\n\n**结语**\n\n未来的科技发展趋势充满挑战与机遇。随着人工智能、量子计算、区块链、生物技术和可持续发展技术等领域的不断进步，我们有望看到一个更加智能、高效、可持续的世界。然而，科技的发展也伴随着伦理、隐私和安全等问题。因此，在追求科技进步的同时，我们也需要关注这些问题，确保科技的发展能够真正造福人类。\n\n---\n', 1, true, 1, 0, '2025-02-24 02:06:17', '2025-02-24 02:06:17', false);

INSERT INTO t_article VALUES (44, 1, 13, E'http://minioapi.overthinker.top/cloud/article/articleCover/b8b2721c-2d45-4bc5-8d2e-81c1bd5ffca8.png', E'城市绿化', E'\n\n\n**构建生态宜居城市的绿色基石**\n\n在快速城市化的今天，高楼大厦如雨后春笋般涌现，为城市带来了前所未有的繁荣。然而，这种繁荣背后也隐藏着诸多问题，如空气污染、热岛效应、生物多样性的减少等。面对这些挑战，城市绿化作为构建生态宜居城市的关键一环，其重要性日益凸显。本文将探讨城市绿化的多重价值、面临的挑战以及实现策略。\n\n**一、城市绿化的多重价值**\n\n1. **改善空气质量**：植物通过光合作用吸收二氧化碳，释放氧气，有效缓解城市空气污染问题。同时，绿植还能吸附空气中的尘埃和有害物质，提升空气质量。\n\n2. **调节城市微气候**：城市绿地如同城市的“天然空调”，通过蒸腾作用降低周围温度，缓解热岛效应。此外，绿地还能增加空气湿度，改善城市小气候。\n\n3. **提升居民生活质量**：绿色空间为城市居民提供了休闲娱乐的好去处，有助于缓解生活压力，提升心理健康。同时，绿地的存在也增强了城市的审美价值，提升了居民的生活满意度。\n\n4. **保护生物多样性**：城市绿地是城市生物多样性的重要栖息地，为鸟类、昆虫等野生动物提供了生存空间，有助于维护生态平衡。\n\n**二、城市绿化面临的挑战**\n\n尽管城市绿化的价值显而易见，但在实施过程中仍面临诸多挑战。首先，城市用地紧张，绿地规划往往受到空间限制。其次，绿化维护成本较高，需要持续的资金投入。此外，公众对绿化的认识和参与度也是影响绿化效果的关键因素。\n\n**三、城市绿化的实现策略**\n\n1. **科学规划，合理布局**：在城市规划中，应将绿地作为城市基础设施的重要组成部分，科学规划，合理布局。通过建设城市公园、街头绿地、屋顶花园等多种形式，增加城市绿地面积。\n\n2. **鼓励公众参与，提升绿化意识**：通过宣传教育、社区活动等方式，提高公众对城市绿化的认识，鼓励居民参与绿化种植、养护等活动，形成全民共建共享的良好氛围。\n\n3. **引入市场机制，创新绿化模式**：探索政府引导、市场运作的绿化模式，吸引社会资本参与城市绿化建设。如通过PPP（政府和社会资本合作）模式，引入专业绿化企业进行绿化项目的投资、建设和运营。\n\n4. **加强科技支撑，提升绿化效率**：运用现代科技手段，如智能灌溉、远程监控等，提高绿化养护的精准度和效率。同时，研发适应城市环境的优良绿化树种，丰富城市绿化品种。\n\n5. **注重生态修复，保护自然遗产**：在城市绿化过程中，应注重对受损生态系统的修复，如利用城市废弃地建设生态公园，既美化城市环境，又保护自然遗产。\n\n**结语**\n\n城市绿化是构建生态宜居城市的重要基石，对于提升城市品质、改善居民生活质量具有不可替代的作用。面对城市化进程中的诸多挑战，我们应积极探索科学的绿化策略，鼓励公众参与，加强科技支撑，共同打造绿色、健康、和谐的城市生态环境。\n\n---\n\n请注意，以上文章为随机生成，仅供参考，具体数据和情况可能因地区和时间的不同而有所变化。', 1, true, 1, 0, '2025-02-24 02:25:46', '2025-02-24 02:25:46', false);

INSERT INTO t_article VALUES (45, 1, 14, E'http://minioapi.overthinker.top/cloud/article/articleCover/3655f106-2452-4403-91bc-ac65b3cfe8c7.png', E'未来工作与职业发展的新趋势', E'\n\n**：迎接变革，塑造未来**\n\n在21世纪的今天，我们正经历着前所未有的职业与工作方式的变革。随着科技的飞速发展、全球化的深入以及社会需求的不断变化，未来的工作与职业发展将呈现出一系列新的趋势。本文将探讨这些趋势，并思考它们如何影响我们的职业道路和工作方式。\n\n**一、远程工作与灵活工作制的兴起**\n\n随着互联网的普及和技术的进步，远程工作已成为越来越多人的选择。特别是在疫情期间，远程工作更是成为了许多行业的标配。未来，随着云计算、大数据、人工智能等技术的进一步发展，远程工作和灵活工作制将更加普及。这种工作方式不仅提高了工作效率，还为员工提供了更多的自由和灵活性，有助于提升工作满意度和生活质量。\n\n**二、技能导向的职业发展**\n\n在未来的职场中，技能将成为衡量个人价值的重要标准。随着技术的不断进步和行业的快速变化，许多传统职业正在被自动化取代，而新兴职业则不断涌现。因此，持续学习和掌握新技能将成为职场人士必备的能力。未来，职业发展将更加注重个人的技能提升和跨界能力，而不仅仅是学历和工作经验。\n\n**三、项目制与团队合作的加强**\n\n随着企业组织结构的扁平化和项目管理的普及，项目制和团队合作将成为未来职场的主流模式。在这种模式下，员工将更多地以项目为单位进行工作，与来自不同背景和专业的团队成员紧密合作。这种工作方式不仅提高了工作效率和创新能力，还促进了员工之间的交流和协作，有助于构建更加和谐、包容的职场文化。\n\n**四、创业与自主就业的兴起**\n\n随着创业环境的改善和自主就业政策的支持，越来越多的人选择创业或自主就业作为自己的职业道路。未来，随着数字化、智能化等技术的进一步发展，创业和自主就业的机会将更加丰富多样。这种职业选择不仅有助于个人实现自我价值，还能为社会创造更多的就业机会和经济增长点。\n\n**五、关注员工福利与心理健康**\n\n在未来的职场中，员工福利和心理健康将受到更多的关注。随着工作压力的增大和生活节奏的加快，员工的身心健康问题日益凸显。因此，企业将更加重视员工福利和心理健康的保障，通过提供健康保险、心理咨询等服务，帮助员工缓解压力、提升幸福感。\n\n**结语**\n\n未来工作与职业发展的新趋势将深刻影响我们的职业道路和工作方式。面对这些变革，我们应保持开放的心态和积极的态度，不断学习和提升自己的技能和能力。同时，我们也应关注自己的身心健康和职业发展需求，努力在变革中找到属于自己的职业道路和工作方式。只有这样，我们才能在未来的职场中立于不败之地，迎接更加美好的未来。\n\n---\n\n', 1, false, 1, 0, '2025-02-24 02:28:18', '2025-02-24 02:28:18', false);

INSERT INTO t_article VALUES (46, 1, 13, E'http://minioapi.overthinker.top/cloud/article/articleCover/e2d1ec3d-5e18-4e42-9fa4-cce9c4d2a1f5.png', E'探索未来教育的创新之路', E'\n\n**科技与人文的融合**\n\n在人类历史的长河中，教育始终是推动社会进步和文明发展的重要力量。随着科技的飞速发展和社会需求的不断变化，未来教育正面临着前所未有的挑战与机遇。本文旨在探讨未来教育的创新之路，特别是科技与人文如何在这一进程中实现深度融合，共同塑造更加公平、高效、个性化的教育体系。\n\n**一、科技驱动的教育变革**\n\n近年来，人工智能、大数据、云计算等先进技术的快速发展，为教育领域带来了深刻的变革。智能教学系统能够根据学生的学习进度和兴趣，提供个性化的学习路径和资源，从而提高学习效率。同时，虚拟现实（VR）、增强现实（AR）等技术的应用，更是为学生创造了沉浸式的学习环境，使抽象知识变得直观易懂。此外，在线学习平台的兴起，打破了地域限制，让优质教育资源得以广泛传播，促进了教育公平。\n\n**二、人文精神的回归与融合**\n\n然而，科技的快速发展也带来了教育领域的某些“异化”现象。过度依赖技术可能导致学生缺乏批判性思维、创新能力和人际交往能力。因此，未来教育在追求技术革新的同时，必须回归人文精神，强调学生的全面发展。这包括培养学生的价值观、社会责任感、审美情趣以及跨文化交流能力。通过人文学科的融入，使教育不仅仅是知识的传授，更是品格的塑造和精神的滋养。\n\n**三、创新教育模式：科技与人文的融合实践**\n\n1. **项目式学习（PBL）**：结合科技手段，如在线协作平台、数据分析工具等，开展跨学科的项目式学习。学生围绕真实世界的问题进行探究，不仅锻炼了问题解决能力，还促进了团队合作和批判性思维的发展。\n\n2. **翻转课堂**：利用视频、在线课程等数字化资源，让学生在课外自主学习基础知识，课堂时间则用于深度讨论、实践操作和个性化指导。这种模式既发挥了科技的优势，又强调了师生互动和人文关怀。\n\n3. **社区参与与社会实践**：鼓励学生参与社区服务、环保项目等社会实践活动，通过亲身体验，增进对社会的理解，培养公民意识和社会责任感。同时，利用科技手段记录、分析和反思这些经历，深化学习效果。\n\n**四、面向未来的教育挑战与应对策略**\n\n尽管科技与人文的融合为教育创新提供了无限可能，但仍面临诸多挑战。如教育资源的分配不均、教师角色的转变与培训、学生自主学习能力的培养等。为此，政府、学校、企业和社会各界需共同努力，制定更加公平、可持续的教育政策，加强教师队伍建设，推动教育技术创新与应用，同时注重学生的人文素养培养。\n\n**结语**\n\n未来教育的创新之路，是一条科技与人文深度融合的探索之旅。在这条路上，我们既要充分利用科技的力量，提升教育的效率和质量，也要坚守人文的底线，关注学生的全面发展。只有这样，我们才能培养出既有扎实知识，又具备创新精神和社会责任感的未来公民，共同迎接更加美好的明天。\n\n---\n', 1, false, 1, 0, '2025-02-24 02:29:38', '2025-02-24 02:29:38', false);

INSERT INTO t_article VALUES (47, 1, 14, E'http://minioapi.overthinker.top/cloud/article/articleCover/8b4eb727-09b8-457d-a710-a1f672a29316.png', E'未来城市', E'\n\n**智能与绿色的交响曲**\n\n在21世纪的曙光中，我们正见证着城市面貌的深刻变革。随着科技的飞速进步和全球环保意识的增强，未来城市正逐步向我们展现出一幅智能与绿色交织的宏伟蓝图。这是一场前所未有的城市革命，它不仅仅是建筑和技术的革新，更是人类生活方式和思维方式的深刻转变。\n\n**一、智能城市的崛起**\n\n智能城市，作为未来城市的核心形态，正在全球范围内迅速崛起。它通过物联网、大数据、云计算、人工智能等先进技术，将城市的基础设施、公共服务、交通出行、环境保护等各个方面进行智能化改造和升级。智能城市不仅提高了城市的运行效率和居民的生活质量，还带来了前所未有的便捷性和安全性。\n\n在智能城市中，交通出行将变得更加智能和高效。自动驾驶汽车、智能交通管理系统、智能停车系统等技术的应用，将大大缓解城市交通拥堵问题，提高出行效率。同时，智能城市还能通过大数据分析，预测和应对各种城市问题，如公共安全、环境污染等，从而确保城市的稳定和安全。\n\n**二、绿色城市的呼唤**\n\n与智能城市并行发展的，是绿色城市的理念和实践。在全球气候变化和环境污染日益严重的背景下，绿色城市成为了未来城市发展的必然趋势。绿色城市强调可持续发展，注重节能减排、资源循环利用和生态环境保护。\n\n在绿色城市中，绿色建筑将成为主流。这些建筑通过采用先进的节能技术和材料，大幅降低能耗和碳排放。同时，绿色城市还将大力发展可再生能源，如太阳能、风能等，以减少对化石燃料的依赖。此外，绿色城市还注重城市绿化和生态保护，通过建设城市公园、湿地保护区等，为城市居民提供优美的生态环境和休闲空间。\n\n**三、智能与绿色的交响曲**\n\n未来城市的发展，将是智能与绿色交织的交响曲。智能城市的技术优势，将为绿色城市的发展提供有力支持。例如，智能城市中的大数据分析技术，可以帮助城市管理者更好地了解城市能源使用情况，从而制定更加科学的节能减排政策。同时，智能城市中的物联网技术，可以实时监测和控制城市中的各种设备，实现能源的高效利用和资源的循环利用。\n\n而绿色城市的发展理念，也将为智能城市的建设提供重要指导。绿色城市强调可持续发展和生态平衡，这将促使智能城市在发展过程中，更加注重环境保护和生态平衡。同时，绿色城市中的绿色建筑和可再生能源技术，也将为智能城市提供更加环保和可持续的能源解决方案。\n\n**四、未来城市的展望**\n\n未来城市的发展，将是一场智能与绿色的交响曲。在这场交响曲中，我们将看到科技与自然、人类与环境的和谐共生。未来城市将成为一个充满智慧、活力和生机的美好家园，为人类社会带来前所未有的繁荣和进步。\n\n然而，未来城市的发展也面临着诸多挑战。如何平衡智能与绿色的发展关系？如何确保城市发展的可持续性和生态平衡？这些问题需要我们不断探索和实践，以找到最佳的解决方案。\n\n总之，未来城市的发展将是一场充满挑战和机遇的旅程。我们相信，在科技与自然、人类与环境的共同努力下，未来城市一定能够成为一个更加美好、更加繁荣的家园。\n\n---\n', 1, false, 1, 0, '2025-02-24 02:44:16', '2025-02-24 02:44:16', false);

SELECT setval('t_article_id_seq', 47);


-- ----------------------------
-- Table structure for t_article_tag
-- ----------------------------
DROP TABLE IF EXISTS t_article_tag CASCADE;
CREATE TABLE t_article_tag (
  id bigserial NOT NULL,
  article_id bigint NOT NULL,
  tag_id bigint NOT NULL,
  create_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_article_tag IS '文章标签关联表';
COMMENT ON COLUMN t_article_tag.id IS '关系表id';
COMMENT ON COLUMN t_article_tag.article_id IS '文章id';
COMMENT ON COLUMN t_article_tag.tag_id IS '标签id';
COMMENT ON COLUMN t_article_tag.create_time IS '创建时间';
COMMENT ON COLUMN t_article_tag.is_deleted IS '是否删除（false：未删除，true：已删除）';

-- ----------------------------
-- Records of t_article_tag
-- ----------------------------
INSERT INTO t_article_tag VALUES (89, 41, 14, '2025-02-23 18:41:35', false);
INSERT INTO t_article_tag VALUES (90, 42, 14, '2025-02-24 02:02:50', false);
INSERT INTO t_article_tag VALUES (91, 43, 15, '2025-02-24 02:06:17', false);
INSERT INTO t_article_tag VALUES (92, 44, 16, '2025-02-24 02:25:46', false);
INSERT INTO t_article_tag VALUES (93, 45, 16, '2025-02-24 02:28:18', false);
INSERT INTO t_article_tag VALUES (94, 46, 16, '2025-02-24 02:29:38', false);
INSERT INTO t_article_tag VALUES (95, 47, 16, '2025-02-24 02:44:16', false);

SELECT setval('t_article_tag_id_seq', 95);


-- ----------------------------
-- Table structure for t_banners
-- ----------------------------
DROP TABLE IF EXISTS t_banners CASCADE;
CREATE TABLE t_banners (
  id bigserial NOT NULL,
  path varchar(255) NOT NULL,
  size bigint NOT NULL,
  type varchar(50) NOT NULL,
  user_id bigint NOT NULL,
  sort_order int NOT NULL,
  create_time timestamp NOT NULL,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_banners IS '轮播图表';
COMMENT ON COLUMN t_banners.id IS '主键id';
COMMENT ON COLUMN t_banners.path IS '图片路径';
COMMENT ON COLUMN t_banners.size IS '图片大小 (字节)';
COMMENT ON COLUMN t_banners.type IS '图片类型 (MIME)';
COMMENT ON COLUMN t_banners.user_id IS '上传人id';
COMMENT ON COLUMN t_banners.sort_order IS '图片顺序';
COMMENT ON COLUMN t_banners.create_time IS '创建时间';

-- ----------------------------
-- Records of t_banners
-- ----------------------------
INSERT INTO t_banners VALUES (40, E'http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png', 3713281, 'image/png', 1, 2, '2025-02-24 02:59:32');
INSERT INTO t_banners VALUES (41, E'http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png', 4203560, 'image/png', 1, 3, '2025-02-24 03:12:08');

SELECT setval('t_banners_id_seq', 41);


-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS t_category CASCADE;
CREATE TABLE t_category (
  id bigserial NOT NULL,
  category_name varchar(20) NOT NULL,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_category IS '文章分类表';
COMMENT ON COLUMN t_category.id IS '分类id';
COMMENT ON COLUMN t_category.category_name IS '分类名';
COMMENT ON COLUMN t_category.create_time IS '创建时间';
COMMENT ON COLUMN t_category.update_time IS '更新时间';
COMMENT ON COLUMN t_category.is_deleted IS '是否删除（false：未删除，true：已删除）';

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO t_category VALUES (13, E'生活', '2025-02-23 18:38:23', '2025-02-23 18:38:23', false);
INSERT INTO t_category VALUES (14, E'科技', '2025-02-24 02:05:49', '2025-02-24 02:05:49', false);

SELECT setval('t_category_id_seq', 14);


-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS t_comment CASCADE;
CREATE TABLE t_comment (
  id bigserial NOT NULL,
  type smallint NOT NULL,
  type_id bigint NOT NULL,
  parent_id bigint DEFAULT NULL,
  reply_id bigint DEFAULT NULL,
  comment_content text NOT NULL,
  comment_user_id bigint NOT NULL,
  reply_user_id bigint DEFAULT NULL,
  is_check boolean NOT NULL DEFAULT true,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_comment IS '评论表';
COMMENT ON COLUMN t_comment.id IS '评论id';
COMMENT ON COLUMN t_comment.type IS '评论类型 (1文章 2留言板)';
COMMENT ON COLUMN t_comment.type_id IS '类型id';
COMMENT ON COLUMN t_comment.parent_id IS '父评论id';
COMMENT ON COLUMN t_comment.reply_id IS '回复评论id';
COMMENT ON COLUMN t_comment.comment_content IS '评论的内容';
COMMENT ON COLUMN t_comment.comment_user_id IS '评论用户的id';
COMMENT ON COLUMN t_comment.reply_user_id IS '回复用户的id';
COMMENT ON COLUMN t_comment.is_check IS '是否通过 (false否 true是)';
COMMENT ON COLUMN t_comment.create_time IS '评论时间';
COMMENT ON COLUMN t_comment.update_time IS '更新时间';
COMMENT ON COLUMN t_comment.is_deleted IS '是否删除（false：未删除，true：已删除）';

-- ----------------------------
-- Records of t_comment
-- ----------------------------
INSERT INTO t_comment VALUES (43, 2, 4, NULL, NULL, E'666[哭泣]', 1, NULL, true, '2023-11-06 11:30:23', '2023-11-06 11:30:23', false);
INSERT INTO t_comment VALUES (44, 2, 4, 43, 43, E'哈哈哈🤑', 1, 1, true, '2023-11-06 11:32:30', '2023-11-06 11:32:30', false);
INSERT INTO t_comment VALUES (47, 2, 4, 41, 46, E'好像是', 1, 1, true, '2023-11-06 11:35:34', '2023-11-06 11:35:34', false);
INSERT INTO t_comment VALUES (48, 2, 3, NULL, NULL, E'你好啊[扶额]', 1, NULL, true, '2023-12-17 17:13:09', '2023-12-17 17:13:09', false);
INSERT INTO t_comment VALUES (51, 2, 2, NULL, NULL, E'😦', 1, NULL, true, '2024-01-07 21:24:30', '2024-01-07 21:24:30', false);

SELECT setval('t_comment_id_seq', 57);


-- ----------------------------
-- Table structure for t_favorite
-- ----------------------------
DROP TABLE IF EXISTS t_favorite CASCADE;
CREATE TABLE t_favorite (
  id bigserial NOT NULL,
  user_id bigint NOT NULL,
  type smallint NOT NULL,
  type_id bigint NOT NULL,
  is_check boolean NOT NULL DEFAULT true,
  create_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_favorite IS '收藏表';
COMMENT ON COLUMN t_favorite.id IS '收藏id';
COMMENT ON COLUMN t_favorite.user_id IS '收藏的用户id';
COMMENT ON COLUMN t_favorite.type IS '收藏类型(1,文章 2,留言板)';
COMMENT ON COLUMN t_favorite.type_id IS '类型id';
COMMENT ON COLUMN t_favorite.is_check IS '是否有效 (false否 true是)';
COMMENT ON COLUMN t_favorite.create_time IS '收藏时间';
COMMENT ON COLUMN t_favorite.is_deleted IS '是否删除（false：未删除，true：已删除）';

SELECT setval('t_favorite_id_seq', 168);


-- ----------------------------
-- Table structure for t_leave_word
-- ----------------------------
DROP TABLE IF EXISTS t_leave_word CASCADE;
CREATE TABLE t_leave_word (
  id bigserial NOT NULL,
  user_id bigint NOT NULL,
  content text NOT NULL,
  is_check boolean NOT NULL DEFAULT true,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_leave_word IS '留言板表';
COMMENT ON COLUMN t_leave_word.id IS 'id';
COMMENT ON COLUMN t_leave_word.user_id IS '留言用户id';
COMMENT ON COLUMN t_leave_word.content IS '留言内容';
COMMENT ON COLUMN t_leave_word.is_check IS '是否通过 (false否 true是)';
COMMENT ON COLUMN t_leave_word.create_time IS '留言时间';
COMMENT ON COLUMN t_leave_word.update_time IS '更新时间';
COMMENT ON COLUMN t_leave_word.is_deleted IS '是否删除（false：未删除，true：已删除）';

-- ----------------------------
-- Records of t_leave_word
-- ----------------------------
INSERT INTO t_leave_word VALUES (13, 1, E'## 测试比较长的留言\n\n> 下面是一篇 c# 笔记\n\n<center>\n    <h1>C#笔记</h1>\n</center>\n\n\n\n[TOC] \n\n### 1、C#访问修饰符\n\n​\t\t在C#当中的访问修饰符及作用范围如下：\n\n|     访问修饰符     |                        说明                        |\n| :----------------: | :------------------------------------------------: |\n|       public       |              共有访问。不受任何限制。              |\n|      private       |      私有访问。只有本类能访问，实例不能访问。      |\n|     protected      |   保护访问。只限于本类和子类访问，实例不能访问。   |\n|      internal      |      内部访问。只限于本项目访问，其他不能访问      |\n| protected internal | 内部保护访问。只限于本项目或子类访问，其他不能访问 |\n\n​\t\tC#成员类型的可修饰及默认修饰符如下：\n\n| 成员类型  | 默认修饰符 |                           可被修饰                           |\n| :-------: | :--------: | :----------------------------------------------------------: |\n|   enum    |   public   |                             none                             |\n|   class   |  private   | public、protected、internal、private、<br />protected internal |\n| interface |   public   |                             none                             |\n|  struct   |  private   |                  public、internal、private                   |\n\n> public 访问级别最高\n>\n> private 访问级别最低\n\n### 2、this 关键字\n\n看以下代码，有什么问题：\n\n```C#\nclass Strdent\n{\n    private string _name;\t//姓名\t\n    public int _age = 19;\t//年龄\n    public string _cardID = \"145236985674526685\";\t//身份证号码\n    public void SetName(string _name)\n    {\n        _name = _name;\n    }\n}\n```\n\n分析： 在 Student 类中定义了一个 private 成员变量 _name,在 SetName()方法的参数中也定义了一个与之同名的变量 _name。这时编译器会发现成员变量和方法的参数重名了。\n此时，编译器无法分辨代码中出现的这两个 _name 那个是成员变量，哪个是方法中的参数。我们可以借助 this 关键字来解决这个问题。\n\n> this 关键字是指当前对象本身。通过 this 可以引用当前类的成员变量和方法。\n\n因此可以改变以上代码为：\n\n```C#\nclass Strdent\n{\n    private string _name;\t//姓名\t\n    public int _age = 19;\t//年龄\n    public string _cardID = \"145236985674526685\";\t//身份证号码\n    public void SetName(string _name)\n    {\n        this._name = _name;\n    }\n}\n```\n\n> 使用 this 关键字可以解决成员变量和局部变量名称冲突的问题。\n\n### 3、C#属性\n\n#### 3.1、用方法保证数据安全', true, '2024-01-16 12:15:27', '2024-01-16 12:15:27', false);

INSERT INTO t_leave_word VALUES (23, 1, E'# 添加留言板\n\n* 添加测试\n* dddd', true, '2024-01-16 13:10:40', '2024-01-16 13:10:40', false);
INSERT INTO t_leave_word VALUES (24, 1, E'# 添加留言板\n\n* 添加测试aaa', true, '2024-01-16 13:16:24', '2024-01-16 13:16:24', false);

INSERT INTO t_leave_word VALUES (25, 1, E'## 测试比较长的留言\n\n> 下面是一篇 c# 笔记\n\n<center>\n    <h1>C#笔记</h1>\n</center>\n\n\n\n[TOC] \n\n### 1、C#访问修饰符\n\n​\t\t在C#当中的访问修饰符及作用范围如下：\n\n|     访问修饰符     |                        说明                        |\n| :----------------: | :------------------------------------------------: |\n|       public       |              共有访问。不受任何限制。              |\n|      private       |      私有访问。只有本类能访问，实例不能访问。      |\n|     protected      |   保护访问。只限于本类和子类访问，实例不能访问。   |\n|      internal      |      内部访问。只限于本项目访问，其他不能访问      |\n| protected internal | 内部保护访问。只限于本项目或子类访问，其他不能访问 |\n\n​\t\tC#成员类型的可修饰及默认修饰符如下：\n\n| 成员类型  | 默认修饰符 |                           可被修饰                           |\n| :-------: | :--------: | :----------------------------------------------------------: |\n|   enum    |   public   |                             none                             |\n|   class   |  private   | public、protected、internal、private、<br />protected internal |\n| interface |   public   |                             none                             |\n|  struct   |  private   |                  public、internal、private                   |\n\n> public 访问级别最高\n>\n> private 访问级别最低\n\n### 2、this 关键字\n\n看以下代码，有什么问题：\n\n```C#\nclass Strdent\n{\n    private string _name;\t//姓名\t\n    public int _age = 19;\t//年龄\n    public string _cardID = \"145236985674526685\";\t//身份证号码\n    public void SetName(string _name)\n    {\n        _name = _name;\n    }\n}\n```\n\n分析： 在 Student 类中定义了一个 private 成员变量 _name,在 SetName()方法的参数中也定义了一个与之同名的变量 _name。这时编译器会发现成员变量和方法的参数重名了。\n此时，编译器无法分辨代码中出现的这两个 _name 那个是成员变量，哪个是方法中的参数。我们可以借助 this 关键字来解决这个问题。\n\n> this 关键字是指当前对象本身。通过 this 可以引用当前类的成员变量和方法。\n\n因此可以改变以上代码为：\n\n```C#\nclass Strdent\n{\n    private string _name;\t//姓名\t\n    public int _age = 19;\t//年龄\n    public string _cardID = \"145236985674526685\";\t//身份证号码\n    public void SetName(string _name)\n    {\n        this._name = _name;\n    }\n}\n```\n\n> 使用 this 关键字可以解决成员变量和局部变量名称冲突的问题。\n\n### 3、C#属性\n\n#### 3.1、用方法保证数据安全', true, '2024-01-16 13:25:08', '2024-01-16 13:25:08', false);

INSERT INTO t_leave_word VALUES (26, 1, E'## 测试留言Markdown 编写\n\n> 不要报错\n\n> ~~没有bug~~', true, '2024-01-16 13:27:50', '2024-01-16 13:27:50', false);
INSERT INTO t_leave_word VALUES (27, 1, E'### 留言bug 最后测试', true, '2024-01-16 13:29:34', '2024-01-16 13:30:23', false);

SELECT setval('t_leave_word_id_seq', 27);


-- ----------------------------
-- Table structure for t_like
-- ----------------------------
DROP TABLE IF EXISTS t_like CASCADE;
CREATE TABLE t_like (
  id bigserial NOT NULL,
  user_id bigint NOT NULL,
  type smallint NOT NULL,
  type_id bigint NOT NULL,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_like IS '点赞表';
COMMENT ON COLUMN t_like.id IS '点赞表id';
COMMENT ON COLUMN t_like.user_id IS '点赞的用户id';
COMMENT ON COLUMN t_like.type IS '点赞类型(1,文章,2,评论,3留言板)';
COMMENT ON COLUMN t_like.type_id IS '点赞的文章id';
COMMENT ON COLUMN t_like.create_time IS '点赞时间';
COMMENT ON COLUMN t_like.update_time IS '修改时间';

-- ----------------------------
-- Records of t_like
-- ----------------------------
INSERT INTO t_like VALUES (257, 1, 2, 5, '2023-11-06 11:02:40', '2023-11-06 11:02:40');
INSERT INTO t_like VALUES (261, 1, 3, 4, '2023-11-06 11:28:30', '2023-11-06 11:28:30');
INSERT INTO t_like VALUES (262, 1, 3, 5, '2023-11-06 11:28:36', '2023-11-06 11:28:36');
INSERT INTO t_like VALUES (263, 1, 2, 46, '2023-11-06 11:35:39', '2023-11-06 11:35:39');
INSERT INTO t_like VALUES (264, 1, 2, 41, '2023-11-06 11:35:41', '2023-11-06 11:35:41');
INSERT INTO t_like VALUES (269, 1, 2, 26, '2023-12-11 16:44:51', '2023-12-11 16:44:51');

SELECT setval('t_like_id_seq', 298);


-- ----------------------------
-- Table structure for t_link
-- ----------------------------
DROP TABLE IF EXISTS t_link CASCADE;
CREATE TABLE t_link (
  id bigserial NOT NULL,
  user_id bigint NOT NULL,
  name varchar(100) NOT NULL,
  url varchar(100) NOT NULL,
  description varchar(100) NOT NULL,
  background varchar(100) NOT NULL,
  is_check boolean NOT NULL DEFAULT false,
  email varchar(20) NOT NULL,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_link IS '友链表';
COMMENT ON COLUMN t_link.id IS '友链表id';
COMMENT ON COLUMN t_link.user_id IS '用户id';
COMMENT ON COLUMN t_link.name IS '网站名称';
COMMENT ON COLUMN t_link.url IS '网站地址';
COMMENT ON COLUMN t_link.description IS '网站描述';
COMMENT ON COLUMN t_link.background IS '网站背景';
COMMENT ON COLUMN t_link.is_check IS '审核状态（false：未通过，true：已通过）';
COMMENT ON COLUMN t_link.email IS '邮箱地址';
COMMENT ON COLUMN t_link.create_time IS '创建时间';
COMMENT ON COLUMN t_link.update_time IS '更新时间';
COMMENT ON COLUMN t_link.is_deleted IS '是否删除（false：未删除，true：已删除）';

-- ----------------------------
-- Records of t_link
-- ----------------------------
INSERT INTO t_link VALUES (18, 1, E'无语小站', E'  http://localhost:99/', E'无语小站无语小站', E'http://cdn.kuailemao.lielfw.cn/articleCover/21676717033297579.jpg', true, E'3490223402@qq.com', '2024-01-22 21:55:08', '2024-01-22 21:55:36', false);

SELECT setval('t_link_id_seq', 18);


-- ----------------------------
-- Table structure for t_photo
-- ----------------------------
DROP TABLE IF EXISTS t_photo CASCADE;
CREATE TABLE t_photo (
  id bigserial NOT NULL,
  user_id bigint NOT NULL,
  name varchar(20) NOT NULL,
  description varchar(50) DEFAULT NULL,
  type smallint NOT NULL,
  parent_id bigint DEFAULT NULL,
  url varchar(255) DEFAULT NULL,
  is_check boolean NOT NULL DEFAULT true,
  size double precision DEFAULT NULL,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_photo IS '相册照片表';
COMMENT ON COLUMN t_photo.id IS '自增id';
COMMENT ON COLUMN t_photo.user_id IS '创建者id';
COMMENT ON COLUMN t_photo.name IS '名称';
COMMENT ON COLUMN t_photo.description IS '描述';
COMMENT ON COLUMN t_photo.type IS '类型（1：相册 2：照片）';
COMMENT ON COLUMN t_photo.parent_id IS '父相册id';
COMMENT ON COLUMN t_photo.url IS '图片地址';
COMMENT ON COLUMN t_photo.is_check IS '是否通过 (false否 true是)';
COMMENT ON COLUMN t_photo.size IS '照片体积大小(kb)';
COMMENT ON COLUMN t_photo.create_time IS '创建时间';
COMMENT ON COLUMN t_photo.update_time IS '更新时间';
COMMENT ON COLUMN t_photo.is_deleted IS '是否删除（false：未删除，true：已删除）';

-- ----------------------------
-- Records of t_photo
-- ----------------------------
INSERT INTO t_photo VALUES (152, 1, E'minio', E'test', 1, NULL, NULL, true, NULL, '2025-02-23 14:41:59', '2025-02-23 14:42:03', false);
INSERT INTO t_photo VALUES (153, 1, E'OIP-C', NULL, 2, 152, E'http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg', true, 0.02, '2025-02-23 14:42:10', '2025-02-23 14:42:10', false);

SELECT setval('t_photo_id_seq', 153);


-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS t_tag CASCADE;
CREATE TABLE t_tag (
  id bigserial NOT NULL,
  tag_name varchar(20) NOT NULL,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_tag IS '标签表';
COMMENT ON COLUMN t_tag.id IS '标签id';
COMMENT ON COLUMN t_tag.tag_name IS '标签名称';
COMMENT ON COLUMN t_tag.create_time IS '标签创建时间';
COMMENT ON COLUMN t_tag.update_time IS '标签更新时间';
COMMENT ON COLUMN t_tag.is_deleted IS '是否删除（false：未删除，true：已删除）';

-- ----------------------------
-- Records of t_tag
-- ----------------------------
INSERT INTO t_tag VALUES (14, E'测试标签', '2025-02-23 18:38:13', '2025-02-23 18:38:13', false);
INSERT INTO t_tag VALUES (15, E'人工智能', '2025-02-24 02:06:04', '2025-02-24 02:06:04', false);
INSERT INTO t_tag VALUES (16, E'社会', '2025-02-24 02:25:30', '2025-02-24 02:25:30', false);

SELECT setval('t_tag_id_seq', 16);


-- ----------------------------
-- Table structure for t_tree_hole
-- ----------------------------
DROP TABLE IF EXISTS t_tree_hole CASCADE;
CREATE TABLE t_tree_hole (
  id bigserial NOT NULL,
  user_id bigint NOT NULL,
  content varchar(100) NOT NULL,
  is_check boolean NOT NULL DEFAULT true,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_tree_hole IS '树洞表';
COMMENT ON COLUMN t_tree_hole.id IS '树洞表id';
COMMENT ON COLUMN t_tree_hole.user_id IS '用户id';
COMMENT ON COLUMN t_tree_hole.content IS '内容';
COMMENT ON COLUMN t_tree_hole.is_check IS '是否通过 (false否 true是)';
COMMENT ON COLUMN t_tree_hole.create_time IS '创建时间';
COMMENT ON COLUMN t_tree_hole.update_time IS '修改时间';
COMMENT ON COLUMN t_tree_hole.is_deleted IS '是否删除（false：未删除，true：已删除）';

-- ----------------------------
-- Records of t_tree_hole
-- ----------------------------
INSERT INTO t_tree_hole VALUES (1, 1, E'测试添加', true, '2023-10-30 11:32:30', '2023-10-30 11:32:30', false);
INSERT INTO t_tree_hole VALUES (29, 1, E'真的是服了！！', true, '2023-10-30 16:41:15', '2023-10-30 16:41:15', false);
INSERT INTO t_tree_hole VALUES (30, 1, E'记得一定要快乐啊！！', true, '2023-10-30 16:41:57', '2024-01-19 21:31:21', false);
INSERT INTO t_tree_hole VALUES (34, 1, E'天天开心', true, '2024-01-19 21:33:24', '2024-01-19 21:33:24', false);

SELECT setval('t_tree_hole_id_seq', 34);


-- ----------------------------
-- Table structure for t_video
-- ----------------------------
DROP TABLE IF EXISTS t_video CASCADE;
CREATE TABLE t_video (
  id bigserial NOT NULL,
  user_id bigint NOT NULL,
  category_id bigint NOT NULL,
  vedio_cover varchar(255) NOT NULL,
  vedio varchar(255) NOT NULL,
  vedio_title varchar(255) NOT NULL,
  vedio_description varchar(255) DEFAULT NULL,
  vedio_type varchar(255) NOT NULL,
  permission smallint NOT NULL,
  status smallint NOT NULL DEFAULT 1,
  vedio_size varchar(255) NOT NULL,
  vedio_count bigint NOT NULL DEFAULT 0,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted smallint NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

COMMENT ON TABLE t_video IS '视频表';
COMMENT ON COLUMN t_video.id IS '唯一id';
COMMENT ON COLUMN t_video.user_id IS '用户id';
COMMENT ON COLUMN t_video.category_id IS '分类';
COMMENT ON COLUMN t_video.vedio_cover IS '视频封面';
COMMENT ON COLUMN t_video.vedio IS '视频地址';
COMMENT ON COLUMN t_video.vedio_title IS '视频标题';
COMMENT ON COLUMN t_video.vedio_description IS '视频介绍';
COMMENT ON COLUMN t_video.vedio_type IS '视频格式';
COMMENT ON COLUMN t_video.permission IS '视频权限1用户专属，2公开';
COMMENT ON COLUMN t_video.status IS '状态1 不知道干什么';
COMMENT ON COLUMN t_video.vedio_size IS '大小（单位自动转换）';
COMMENT ON COLUMN t_video.vedio_count IS '视频访问量';
COMMENT ON COLUMN t_video.create_time IS '创建时间';
COMMENT ON COLUMN t_video.update_time IS '修改时间';
COMMENT ON COLUMN t_video.is_deleted IS '是否删除默认1 没有删除 ，2 已删除（注：原表注释与默认值存在矛盾，已保留数值类型不做布尔转换，避免业务异常）';