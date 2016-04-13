#include <string.h>
#include <iostream>
#include <deque>
#include <sstream>

#include "share/utility.h"
#include "control.h"
#include "parser.h"

using namespace std;

const char* Parser::Root = "Root";

Parser Parser::Instance;

Parser::Node::Node()
    : Func(NULL)
    , AnyNext(0)
    , End(false)
{}

void Parser::GetMatchList(Node& node, OpMap& result, NodeSet& set, bool all)
{
    if(set.count(&node))
        return;
    if(node.AnyNext)
    {
        ASSERT(result.count("") == 0 || result[""] == node.AnyNext);
        result[""] = node.AnyNext;
    }
    if(all && node.Func)
    {
        StringCollection match;
        try
        {
            node.Func(match);
        } catch(ValueException& e)
        {
            match.clear();
        }
        ENUM_STL(StringCollection, match, e)
        {
            ASSERT(result.count(*e) == 0);
            ASSERT(node.AnyNext);
            result[*e] = node.AnyNext;
        }
    }
    ENUM_STL_CONST(OpMap, node.OP, e)
    {
        ASSERT(result.count(e->first) == 0);
        result[e->first] = e->second;
    }
    ENUM_STL_CONST(IntCollection, node.Empty, e)
    {
        set.insert(&node);
        GetMatchList(FMap[*e], result, set, all);
    }
}

void Parser::GetMatchList(Node& node, OpMap& result, bool all)
{
    NodeSet set;
    GetMatchList(node, result, set, all);
}

void Parser::BindEmpty(int from, int to)
{
    ASSERT(FMap[from].Empty.count(to) == 0);
    FMap[from].Empty.insert(to);
}

void Parser::BindOp(int from, int to, const char* op)
{
    ASSERT(FMap[from].OP.count(op) == 0);
    FMap[from].OP[op] = to;
}

void Parser::BindAny(int from, int to, CompletionFunc func)
{
    Node& node = FMap[from];
    ASSERT(!node.Func);
    node.Func = func;
    node.AnyNext = to;
}

Parser::Rule::Rule()
    : Type(Unknown)
    , Func(NULL)
{}

int Parser::RandomNode()
{
    ASSERT(FMap.size() < RAND_MAX - 1);
    int res = random();
    while(FMap.count(res) && res != 0)
        res = random();
    return res;
}

void Parser::ExecuteRule(int left, int right, const Rule& rule)
{
    switch(rule.Type)
    {
        case Rule::And:
            {
                ASSERT(!rule.Data.empty());
                StringList data(rule.Data);
                int r = right;
                FMap[r];
                while(!data.empty())
                {
                    int l = (data.size() == 1) ? left : RandomNode();
                    FMap[l];
                    ExecuteRule(l, r, FRules[data.back()]);
                    data.pop_back();
                    r = l;
                }
            }
            break;
        case Rule::Or:
            {
                ASSERT(!rule.Data.empty());
                ENUM_STL_CONST_R(StringList, rule.Data, e)
                {
                    ExecuteRule(left, right, FRules[*e]);
                }
            }
            break;
        case Rule::Any:
            {
                ASSERT(rule.Data.size() == 1);
                int temp = RandomNode();
                FMap[left].End = FMap[temp].End = FMap[right].End;
                BindEmpty(left, temp);
                BindEmpty(temp, left);
                BindEmpty(temp, right);
                ExecuteRule(left, temp, FRules[rule.Data.front()]);
            }
            break;
        case Rule::Little:
            {
                ASSERT(rule.Data.size() == 1);
                FMap[left].End = FMap[right].End;
                BindEmpty(left, right);
                ExecuteRule(left, right, FRules[rule.Data.front()]);
            }
            break;
        case Rule::Op:
            {
                ASSERT(rule.Data.size() == 1);
                BindOp(left, right, rule.Data.front().c_str());
            }
            break;
        case Rule::Value:
            {
                ASSERT(rule.Data.empty());
                BindAny(left, right, rule.Func);
            }
            break;
        default:
            ASSERT(false);
            break;
    }
}

String Parser::RandomRule()
{
    String temp = "__" + RandomString();
    while(FRules.count(temp))
        temp = "__" + RandomString();
    return temp;
}

int Parser::FindOp(const OpList& list, int left, int right, const char* op)
{
    int dep = 0;
    for(int pos = right; pos >= left; --pos)
    {
        if(list[pos] == ")")
            ++dep;
        else if(list[pos] == "(")
            --dep;
        else if(dep == 0 && list[pos] == op)
            return pos;
    }
    return -1;
}

void Parser::SetRule(const char* name, const OpList& op, int left, int right)
{
    if(right > left)
    {
        int pos;
        pos = FindOp(op, left, right, "&");
        if(pos != -1)
        {
            String l = RandomRule();
            FRules[l];
            String r = RandomRule();
            FRules[r];
            ASSERT(left <= pos - 1);
            SetRule(l.c_str(), op, left, pos - 1);
            ASSERT(right >= pos + 1);
            SetRule(r.c_str(), op, pos + 1, right);
            SetAndRule(name, (l + " " + r).c_str());
            return;
        }
        pos = FindOp(op, left, right, "|");
        if(pos != -1)
        {
            String l = RandomRule();
            FRules[l];
            String r = RandomRule();
            FRules[r];
            ASSERT(left <= pos - 1);
            SetRule(l.c_str(), op, left, pos - 1);
            ASSERT(right >= pos + 1);
            SetRule(r.c_str(), op, pos + 1, right);
            SetOrRule(name, (l + " " + r).c_str());
            return;
        }
        if(op[left] == "(")
        {
            SetRule(name, op, left + 1, right - 1);
            return;
        }
        if(op[left] == "*")
        {
            String temp = RandomRule();
            FRules[temp];
            SetRule(temp.c_str(), op, left + 1, right);
            SetAnyRule(name, temp.c_str());
            return;
        }
        if(op[left] == "?")
        {
            String temp = RandomRule();
            FRules[temp];
            SetRule(temp.c_str(), op, left + 1, right);
            SetLittleRule(name, temp.c_str());
            return;
        }
        if(op[left] == "~")
        {
            ASSERT(right - left == 1);
            SetAndRule(name, op[right].c_str());
            return;
        }
        ASSERT(false);
    } else
        SetOpRule(name, op[left].c_str());
}

void Parser::SetRule(const char* name, const char* rule)
{
    ostringstream stream;
    while(*rule != 0)
    {
        switch(*rule)
        {
            case '(':
            case ')':
            case '|':
            case '*':
            case '?':
            case '~':
                stream << " " << *rule << " ";
                break;
            default:
                stream << *rule;
        }
        ++rule;
    }
    OpList list;
    istringstream parsestream(stream.str());
    String word;
    while(parsestream >> word)
    {
        if(word == "(")
        {
            if(!list.empty() && list.back() != "(" && list.back() != "*" && list.back() != "?" && list.back() != "~" && list.back() != "|")
                list.push_back("&");
        } else if(word == "*" || word == "?" || word == "~")
        {
            if(!list.empty() && list.back() != "(" && list.back() != "|" && list.back() != "?" && list.back() != "*")
                list.push_back("&");
        } else if(word != ")" && word != "|" && word != "*" && word != "?" && word != "~")
        {
            if(!list.empty()  && list.back() != "(" && list.back() != "|" && list.back() != "*" && list.back() != "?" && list.back() != "~")
                list.push_back("&");
        }
        list.push_back(word);
    }
    ASSERT(!list.empty());
#ifdef __DEBUG__
    cout << endl;
    ENUM_STL(OpList, list, e)
    {
        cout << *e << " ";
    }
    cout << endl;
#endif
    SetRule(name, list, 0, list.size() - 1);
}

void Parser::SetOrderRule(OStream& stream, OrderRule& order, int min, StringCollection& must, int count)
{
    if(count == 0)
        return;
    if(min <= 0 && must.empty())
        stream << "?(";
    bool first = true;
    ENUM_STL(OrderRule, order, e)
    {
        if(e->second)
        {
            if(!first)
                stream << "|";
            else
                first = false;
            stream << "(~" << e->first << " ";
            bool match = must.count(e->first);
            e->second = false;
            if(match)
                must.erase(e->first);
            SetOrderRule(stream, order, match ? min : min - 1, must, count - 1);
            if(match)
                must.insert(e->first);
            e->second = true;
            stream << ")";
        }
    }
    if(min <= 0 && must.empty())
        stream << ")";
}

void Parser::SetOrderRule(const char* name, const char* ordername, int mincount, const char* mustname)
{
    OrderRule order;
    {
        istringstream stream(ordername);
        String word;
        while(stream >> word)
            order.insert(OrderRule::value_type(word, true));
    }
    StringCollection must;
    {
        if(mustname)
        {
            istringstream stream(mustname);
            String word;
            while(stream >> word)
            {
                ASSERT(order.count(word));
                must.insert(word);
            }
        }
    }
    ASSERT(mincount + must.size() <= order.size());
    ostringstream stream;
    SetOrderRule(stream, order, mincount, must, order.size());
    PRINTF(stream.str());
    SetRule(name, stream.str().c_str());
}

void Parser::SetOpRule(const char* name, const char* op)
{
    Rule& rule = FRules[name];
    rule.Type = Rule::Op;
    ASSERT(rule.Data.empty());
    rule.Data.push_back(op);
}

void Parser::SetAndRule(const char* name, const char* andname)
{
    Rule& rule = FRules[name];
    rule.Type = Rule::And;
    ASSERT(rule.Data.empty());
    istringstream stream(andname);
    String value;
    while(stream >> value)
        rule.Data.push_back(value);
}

void Parser::SetOrRule(const char* name, const char* orname)
{
    Rule& rule = FRules[name];
    rule.Type = Rule::Or;
    ASSERT(rule.Data.empty());
    istringstream stream(orname);
    String value;
    while(stream >> value)
        rule.Data.push_back(value);
}

void Parser::SetAnyRule(const char* name, const char* anyname)
{
    Rule& rule = FRules[name];
    rule.Type = Rule::Any;
    ASSERT(rule.Data.empty());
    rule.Data.push_back(anyname);
}

void Parser::SetLittleRule(const char* name, const char* littlename)
{
    Rule& rule = FRules[name];
    rule.Type = Rule::Little;
    ASSERT(rule.Data.empty());
    rule.Data.push_back(littlename);
}

void Parser::SetValueRule(const char* name, CompletionFunc func)
{
    Rule& rule = FRules[name];
    rule.Type = Rule::Value;
    ASSERT(rule.Data.empty());
    rule.Func = func;
}

void Parser::Init()
{
    FMap[0];
    FMap[1].End = true;
    ExecuteRule(0, 1, FRules[Root]);
    FRules.clear();
}

void Parser::GetMatchCollection(const StringList& cmd, StringCollection& match)
{
    StringList ops(cmd);
    Node* node = &(FMap[0]);
    while(true)
    {
        OpMap rule;
        GetMatchList(*node, rule, ops.empty());
        if(!ops.empty())
        {
            if(rule.count(ops.front()))
                node = &(FMap[rule[ops.front()]]);
            else if(rule.count(""))
                node = &(FMap[rule[""]]);
            else
                break;
        } else {
            ENUM_STL(OpMap, rule, e)
            {
                if(!e->first.empty())
                    match.insert(e->first);
            }
            if(match.size() == 1 && (!node->Empty.empty() || node->AnyNext))
                match.insert("");
            break;
        }
        ops.pop_front();
    }
}

void Parser::ParseCmd(const StringList& cmd, ::OpList& list)
{
    list.clear();
    StringList ops(cmd);
    Node* node = &(FMap[0]);
    ENUM_STL_CONST(StringList, cmd, e)
    {
        OpMap rule;
        GetMatchList(*node, rule, false);
        if(!e->empty() && rule.count(*e))
        {
            node = &(FMap[rule[*e]]);
            list.push_back(::OpList::value_type(*e, true));
        } else if(rule.count(""))
        {
            node = &(FMap[rule[""]]);
            list.push_back(::OpList::value_type(*e, false));
        } else
            ERROR(Exception::Command::Op, *e);
    }
    if(!node->End)
        ERROR(Exception::Command::NotMatch, "");
}

