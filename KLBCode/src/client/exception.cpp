#ifdef __DEBUG__
#include <iostream>
#endif

#include <map>
#include <sstream>

#include "share/include.h"

#include "model/hvs.h"
#include "model/exception.h"

#include "exception.h"

using namespace std;

namespace Exception
{
    class MessageBox
    {
        private:
            typedef struct
            {
                int Count;
                const char* FormatStr;
            } FormatData;
            typedef map<int, FormatData> FormatMap;
            FormatMap FList;
        private:
            const FormatData* GetFormat(int code) const
            {
                FormatMap::const_iterator pos = FList.find(code);
                if(pos == FList.end())
                    return NULL;
                else
                    return &(pos->second);
            }
        protected:
            void Declare(int code, const char* format, int count)
            {
                ASSERT(FList.count(code) == 0);
                FormatData& data = FList[code];
                data.Count = count;
                data.FormatStr = format;
            }
        public:
            virtual const char* Default() const = 0;
            String Get(ValueException& error) const
            {
                const FormatData* format = GetFormat(error.getCode());
                if(format)
                {
                    const String& msg = error.getMessage();
                    istringstream stream(msg);
                    String param1, param2, param3;
                    getline(stream, param1);
                    getline(stream, param2);
                    getline(stream, param3);
                    char buf[strlen(format->FormatStr) + msg.size()];
                    switch(format->Count)
                    {
                        case 1:
                            sprintf(buf, format->FormatStr, param1.c_str());
                            break;
                        case 2:
                            sprintf(buf, format->FormatStr, param1.c_str(), param2.c_str());
                            break;
                        case 3:
                            sprintf(buf, format->FormatStr, param1.c_str(), param2.c_str(), param3.c_str());
                            break;
                        default:
                            sprintf(buf, format->FormatStr);
                    }
                    return buf;
                } else {
                    const char* defaultformat = Default();
                    char buf[strlen(defaultformat) + 20];
                    sprintf(buf, defaultformat, error.getCode());
                    return String(buf);
                }
            }
            virtual ~MessageBox()
            {}
#ifdef __DEBUG__
            void GetList()
            {
                ENUM_STL(FormatMap, FList, item)
                {
                    cout << "_format.put(" << item->first << ", \"" << item->second.FormatStr << "\");" << endl;
                    cout << "_count.put(" << item->first << ", " << item->second.Count << ");" << endl;
                }
            }
#endif
    };

    class EnglishMessageBox: public MessageBox
    {
        public:
            virtual const char* Default() const
            {
                return "Unknown exception. (code: %d)";
            }
    } English;

    class ChineseUTF8MessageBox: public MessageBox
    {
        public:
            ChineseUTF8MessageBox()
            {
                Declare(Command::NotMatch, "命令不匹配.", 0);
                Declare(Command::Op, "%s 为无效操作符.", 1);
                Declare(Command::IntValue, "%s 不是有效整数.", 1);
                Declare(Command::Connect, "无法连接KLB管理程序.", 0);
                Declare(Command::PasswordAgain, "两次输入的密码不一致.", 0);
                Declare(Command::TimeFormat, "时间格式不正确.", 0);
                Declare(Command::ZoneFormat, "时区格式不正确.", 0);
                Declare(Command::DelIP, "接口 %s 不存在IP %s.", 2);
                Declare(Command::ISPName, "ISP名称 %s 不存在.", 1);
                Declare(Server::Password, "密码无效.", 0);
                Declare(Server::Params, "%s 参数无效.", 1);
                Declare(Server::Version, "版本不匹配.", 0);
                Declare(Server::Licence, "Licence无效，请注册.", 0);
                Declare(Server::ImportLicence, "%s 为无效Licence.", 1);
                Declare(Types::RangedInt, "整数 %s 值无效.", 1);
                Declare(Types::RangedState, "%s 值超出枚举范围.", 1);
                Declare(Types::SpecialString, "字符串 %s 格式无效.", 1);
                Declare(Types::Binary, "二进制数据过长.", 0);
                Declare(Types::RangedList, "索引 %s 超过范围.", 1);
                Declare(Types::GetList::Start, "获取列表的起始参数 %s 无效.", 1);
                Declare(Types::GetList::Count, "获取列表的长度参数 %s 无效.", 1);
                Declare(System::Time, "设置时间失败.", 0);
                Declare(System::ImportConfigure, "导入配置文件失败.", 0);
                Declare(System::SaveConfigure, "配置文件保存失败.", 0);
                Declare(Interface::NotFoundDev, "接口 %s 不存在.", 1);
                Declare(Interface::ExistDev, "接口 %s 已存在.", 1);
                Declare(Interface::MTU, "接口 %s 设置MTU %s 失败.", 2);
                Declare(Interface::Enabled, "开启接口 %s 失败.", 1);
                Declare(Interface::Disabled, "关闭接口 %s 失败.", 1);
                Declare(Interface::Address, "接口 %s 设置物理地址 %s 失败.", 2);
                Declare(Interface::IP::Flush, "清除接口 %s IP地址失败.", 1);
                Declare(Interface::IP::Add, "接口 %s 添加IP %s 失败.", 2);
                Declare(Interface::IP::Dhcp, "接口 %s 已开启DHCP，无法设置IP.", 1);
                Declare(Interface::IP::Duplicate, "接口 %s 已存在IP %s ，设置失败.", 2);
                Declare(Interface::Ethernet::DetectOn, "接口 %s 开启协商失败.", 1);
                Declare(Interface::Ethernet::DetectOff, "接口 %s 关闭协商失败.", 1);
                Declare(Interface::Ethernet::DuplexSpeed, "接口 %s 指定工作模式失败.", 1);
                Declare(Interface::Ethernet::Slave, "接口 %s 为接口 %s 的从接口，设置失败.", 2);
                Declare(Interface::Ethernet::Adsl, "接口 %s 已属于PPPoE接口 %s ，设置失败.", 2);
                Declare(Interface::Bonding::Slave::Add, "在接口 %s 添加从接口 %s 失败.", 2);
                Declare(Interface::Bonding::Slave::Del, "在接口 %s 删除从接口 %s 失败.", 2);
                Declare(Interface::Bonding::Slave::Empty, "接口 %s 无从接口.", 1);
                Declare(Interface::Bonding::CheckIP::Add, "在接口 %s 添加检测IP %s 失败.", 2);
                Declare(Interface::Bonding::CheckIP::Del, "在接口 %s 删除检测IP %s 失败.", 2);
                Declare(Interface::Bonding::Mode::Set, "接口 %s 设置聚合模式失败.", 1);
                Declare(Interface::Bonding::Mode::Dot, "从接口 %s 不支持802.3ad模式.", 1);
                Declare(Interface::Bonding::Mode::ALB, "从接口 %s 不支持alb平衡模式.", 1);
                Declare(Interface::Bonding::Mode::TLB, "从接口 %s 不支持tlb平衡模式.", 1);
                Declare(Interface::Bonding::LinkCheck, "接口 %s 设置链路检查模式失败.", 1);
                Declare(Interface::Bonding::Add, "添加聚合接口 %s 失败.", 1);
                Declare(Interface::Bonding::Del, "删除聚合接口 %s 失败.", 1);
                Declare(Interface::Adsl::Used, "物理接口 %s 已被使用.", 1);
                Declare(Interface::Adsl::Ethernet, "物理接口 %s 无法启动.", 1);
                Declare(Interface::Adsl::Busy, "PPPoE接口 %s 正忙.", 1);
                Declare(Interface::Adsl::User, "PPPoE接口 %s 的用户名为空.", 1);
                Declare(Interface::Adsl::Password, "PPPoE接口 %s 的密码为空.", 1);
                Declare(Interface::Adsl::Stopped, "PPPoE接口 %s 已停止.", 1);
                Declare(Interface::Adsl::Dial, "PPPoE接口 %s 正在拨号.", 1);
                Declare(Interface::Adsl::Connected, "PPPoE接口 %s 已连接.", 1);
                Declare(Updater::Reset, "初始化更新失败.", 0);
                Declare(Updater::Upload, "数据上传失败.", 0);
                Declare(Updater::Failed, "更新失败.", 0);
                Declare(Network::Hostname, "主机名不能为空.", 0);
                Declare(Network::DnsServer, "DNS服务器配置失败.", 0);
                Declare(Network::Protocol, "%s 不是有效的协议名称或编号.", 1);
                Declare(Network::Service, "%s 不是有效的端口名称或端口号.", 1);
                Declare(Network::SupportPort, "%s 协议不支持使用端口.", 1);
                Declare(Arp::Set, "设置静态ARP项失败.", 0);
                Declare(Arp::Del, "删除ARP项失败.", 0);
                Declare(ISP::NotFoundID, "无法找到ISP，ID %s .", 1);
                Declare(ISP::Name::Empty, "ISP名称不能为空.", 0);
                Declare(ISP::Name::Duplicate, "ISP名称 %s 已使用.", 1);
                Declare(ISP::EmptyNet, "ISP目标网段不能为空.", 0);
                Declare(ISP::Count, "ISP数量过多.", 0);
                Declare(Bind::Domain::Empty, "域名不能为空.", 0);
                Declare(Bind::Domain::NotFound, "域名 %s 不存在.", 1);
                Declare(Bind::Domain::Duplicate, "域名 %s 已存在.", 1);
                Declare(Bind::Alias::Empty, "别名不能为空.", 0);
                Declare(Bind::Alias::Duplicate, "别名或域名 %s 已存在.", 1);
                Declare(Bind::Server::ISP, "服务器 %s 的ISP %s 不存在.", 2);
                Declare(Bind::Server::Empty, "服务器IP不能为空.", 0);
                Declare(Nat::Map::IP, "映射IP不能为空.", 0);
                Declare(Nat::Map::Port, "映射的端口范围 %s-%s 无效.", 2);
                Declare(Route::Gate::DetectDev, "检测网关 %s 所属接口失败.", 1);
                Declare(Route::Gate::EmptyDev, "网关接口不能为空.", 0);
                Declare(Route::Static::Empty, "路由目标不能为空.", 0);
                Declare(Route::Static::Duplicate, "路由 %s 度量值 %s 已存在", 2);
                Declare(Route::Static::NotFound, "路由 %s 度量值 %s 不存在", 2);
                Declare(Route::Policy::Count, "策略路由条目过多.", 0);
                Declare(Route::Smart::Duplicate, "ISP ID: %s 已存在.", 1);
                Declare(Route::Smart::Count, "智能路由条目过多.", 0);
                Declare(IPVS::HA::Running, "高可用模块正在运行.", 0);
                Declare(IPVS::HA::Time, "心跳检查时间参数配置错误.", 0);
                Declare(IPVS::HA::Dev::Empty, "心跳接口不能为空.", 0);
                Declare(IPVS::HA::Dev::Status, "心跳接口 %s 无效.", 1);
                Declare(IPVS::HA::Hostname, "心跳对方的主机名不能为空.", 0);
                Declare(IPVS::HA::IP, "心跳对方的IP不能为空.", 0);
                Declare(IPVS::Service::DuplicateMark, "虚拟服务的标记值 %s 已存在.", 1);
                Declare(IPVS::Service::Count, "虚拟服务数量过多.", 0);
                Declare(IPVS::Service::Ports, "虚拟服务端口范围 %s-%s 无效.", 2);
                Declare(IPVS::Service::IP::Duplicate, "虚拟服务的IP %s 已存在.", 1);
                Declare(IPVS::Service::IP::Empty, "虚拟服务的IP不能为空.", 0);
                Declare(IPVS::Service::Address, "虚拟服务地址重复.", 0);
                Declare(IPVS::Service::Name::Duplicate, "虚拟服务名称 %s 已存在.", 1);
                Declare(IPVS::Service::Name::Invalid, "虚拟服务名称 %s 无效.", 1);
                Declare(IPVS::Service::Name::NotFound, "虚拟服务名称 %s 未找到.", 1);
                Declare(IPVS::Service::Name::Empty, "虚拟服务名称为空.", 0);
                Declare(IPVS::Server::DuplicateID, "服务器ID %s 已存在.", 1);
                Declare(IPVS::Server::Name::Duplicate, "服务器名称 %s 已存在.", 1);
                Declare(IPVS::Server::Name::Invalid, "服务器名称 %s 无效.", 1);
                Declare(IPVS::Server::Name::NotFound, "服务器名称 %s 未找到.", 1);
                Declare(IPVS::Server::Name::Empty, "服务器名称为空.", 0);
                Declare(IPVS::Server::IP::Duplicate, "服务器IP %s 已存在.", 1);
                Declare(IPVS::Server::IP::Empty, "服务器IP不能为空.", 0);
                Declare(IPVS::Statistic::Range, "数据统计的时间范围 %s-%s 无效.", 2);
                Declare(IPVS::Statistic::Interval, "数据统计间隔 %s 无效.", 1);
                Declare(IPVS::Statistic::ID, "虚拟服务ID %s 无效.", 1);
                Declare(IPVS::Statistic::IP, "服务器IP %s 无效.", 1);
                Declare(Http::NameEmpty, "虚拟服务名称为空.", 0);
                Declare(Http::NameDuplicate, "虚拟服务器 %s 已存在.", 1);
                Declare(Http::AddressEmpty, "IP或接口为空.", 0);
                Declare(Http::AddressDuplicate, "地址 %s %s 无效.", 2);
                Declare(Http::LocationMatchEmpty, "匹配规则为空.", 0);
                Declare(Http::LocationGroupNotFound, "服务器组 %s 不存在.", 1);
                Declare(Http::GroupNameEmpty, "服务器组名为空.", 0);
                Declare(Http::GroupNameDuplicate, "服务器组 %s 已存在.", 1);
                Declare(Http::ServerIPEmpty, "服务器IP地址为空.", 0);
                Declare(Http::GroupUsed, "服务器组正在被 %s 服务使用.", 1);
                Declare(Http::PortUsed, "端口 %s 已被占用.", 1);
            }
            virtual const char* Default() const
            {
                return "未知错误. (错误号: %d)";
            }
    } ChineseUTF8;

    String GetMessage(ValueException& error)
    {
        const char* lang = getenv("LANG");
        if(lang)
        {
            if(strcasecmp(lang, "zh_CN.UTF-8") == 0)
                return ChineseUTF8.Get(error);
            else if(strcasecmp(lang, "zh_CN.UTF8") == 0)
                return ChineseUTF8.Get(error);
        }
        return English.Get(error);
    }

#ifdef __DEBUG__
    void GetMessageList()
    {
        ChineseUTF8.GetList();
    }
#endif

}
