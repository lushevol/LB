#ifndef __MODEL_H__
#define __MODEL_H__

#include <deque>
#include <set>
#include <sstream>
#include <string>

#include "share/include.h"
#include "rpc/XmlRpc.h"
#include "exception.h"

#ifndef STRINGTYPE
#define STRINGTYPE
typedef std::string String;
#endif

#ifndef STRINGLISTTYPE
#define STRINGLISTTYPE
typedef std::deque<String> StringList;
#endif

#ifndef STRINGCOLLETIONTYPE
#define STRINGCOLLETIONTYPE
typedef std::set<String> StringCollection;
#endif

#ifndef INTCOLLETIONTYPE
#define INTCOLLETIONTYPE
typedef std::set<int> IntCollection;
#endif

#ifndef OSTREAMTYPE
#define OSTREAMTYPE
typedef std::ostream OStream;
#endif

typedef XmlRpc::XmlRpcValue Value;
typedef XmlRpc::XmlRpcValue::BinaryData BinaryData;
typedef XmlRpc::XmlRpcValue::ValueArray ValueArray;
typedef XmlRpc::XmlRpcValue::ValueStruct ValueStruct;
typedef XmlRpc::XmlRpcException ValueException;

class Model
{
    protected:
        virtual void CheckValid() const {}
    public:
        Value& Data;
    public:
        Model(Value& data)
            : Data(data)
        {}
        virtual ~Model()
        {}
        bool Valid() const
        {
            if(Data.valid())
            {
                CheckValid();
                return true;
            } else
                return false;
        }
};

template<class Base>
class BaseValue: public Model
{
    protected:
        virtual void CheckValid() const
        {
            if((Base&)Data == (Base&)Data)
            {
            }
        }
    public:
        BaseValue(Value& data)
            : Model(data)
        {
            if(Data.valid() && (Base&)Data == (Base&)Data)
            {
            }
        }
        operator const Base&()
        {
            return (Base&)Data;
        }
        BaseValue<Base>& operator=(const Base& value)
        {
            Base& data = (Base&)Data;
            if(&data == &value)
            {
                Base temp = value;
                data = temp;
            } else
                data = value;
            return *this;
        }
        BaseValue<Base>& operator=(BaseValue<Base>& value)
        {
            *this = (const Base&)value;
            return *this;
        }
        bool operator==(const Base& value)
        {
            return value == (Base&)Data;
        }
        bool operator!=(const Base& value)
        {
            return !(*this == value);
        }
        friend OStream& operator<<(OStream& stream, BaseValue<Base>& value)
        {
            stream << (Base&)(value.Data);
            return stream;
        }
};

typedef BaseValue<int> IntValue;
typedef BaseValue<String> StringValue;
typedef BaseValue<bool> BoolValue;

template < int min, int max, int def = min >
class RangedIntValue: public Model
{
    protected:
        virtual void CheckValid() const
        {
            CheckValue((int&)Data);
        }
    private:
        static void CheckValue(int value)
        {
            if(value < min || value > max)
                ERROR(Exception::Types::RangedInt, value);
        }
        void EnsureData()
        {
            if(Data.valid())
                CheckValue((int&)Data);
            else
                (int&)Data = def;
        }
    public:
        RangedIntValue(Value& data)
            : Model(data)
        {
            if(Data.valid())
                CheckValue((int&)Data);
        }
        operator const int&()
        {
            EnsureData();
            return (int&)Data;
        }
        RangedIntValue<min, max, def>& operator=(int value)
        {
            CheckValue(value);
            (int&)Data = value;
            return *this;
        }
        RangedIntValue<min, max, def>& operator=(RangedIntValue<min, max, def>& value)
        {
            *this = (const int&)value;
            return *this;
        }
        static const int Min = min;
        static const int Max = max;
        static const int Default = def;
};

static const int MaximumInteger = 0x7FFFFFFF;

typedef RangedIntValue<0, MaximumInteger> UnsignedValue;

class State
{
    public:
        virtual ~State() {}
        virtual const char* Get(int state) const = 0 ;
        virtual int Default() const = 0;
};

template<class State>
class StateValue: public Model
{
    private:
        const char* FState;
    public:
        State Type;
    private:
        virtual void CheckValid() const
        {
            GetState((int&)Data);
        }
        const char* GetState(int state) const
        {
            const char* result = Type.Get(state);
            if(!result)
                ERROR(Exception::Types::RangedState, state);
            return result;
        }
        void EnsureData()
        {
            if(!Data.valid())
                (int&)Data = Type.Default();
            FState = GetState((int&)Data);
        }
    public:
        StateValue(Value& data)
            : Model(data)
        {
            if(Data.valid())
                FState = GetState((int&)Data);
            else
                FState = NULL;
        }
        operator const int&()
        {
            EnsureData();
            return (int&)Data;
        }
        StateValue<State>& operator=(int state)
        {
            FState = GetState(state);
            (int&)Data = state;
            return *this;
        }
        StateValue<State>& operator=(StateValue<State>& value)
        {
            *this = (const int&)value;
            return *this;
        }
        const char* Str()
        {
            EnsureData();
            return FState;
        }
};

class SpecialString
{
    public:
        virtual ~SpecialString() {}
        virtual bool Get(const String& value, String& result) = 0 ;
        virtual void Default(String& value) = 0;
        String Default()
        {
            String result;
            Default(result);
            return result;
        }
};

template<class SpecialStringClass>
class SpecialStringValue: public Model
{
    public:
        SpecialStringClass Type;
    private:
        virtual void CheckValid() const
        {
            String result;
            GetValue((String&)Data, result);
        }
    protected:
        void GetValue(const String& value, String& result) const
        {
            SpecialStringClass* type = const_cast<SpecialStringClass*>(&Type);
            if(!type->Get(value, result))
                ERROR(Exception::Types::SpecialString, value);
        }
        void EnsureData()
        {
            if(Data.valid())
            {
                String str(Data);
                Type.Get(str, (String&)Data);
            } else
                Type.Default((String&)Data);
        }
    public:
        SpecialStringValue(Value& data)
            : Model(data)
        {
            if(Data.valid())
                CheckValid();
        }
        operator const String&()
        {
            EnsureData();
            return (String&)Data;
        }
        SpecialStringValue<SpecialStringClass>& operator=(const String& value)
        {
            if(&value == &((String&)Data))
            {
                String temp(value);
                GetValue(temp, (String&)Data);
            } else
                GetValue(value, (String&)Data);
            return *this;
        }
        SpecialStringValue<SpecialStringClass>& operator=(SpecialStringValue<SpecialStringClass>& value)
        {
            *this = (const String&)value;
            return *this;
        }
        bool operator==(const String& value)
        {
            EnsureData();
            return value == (String&)(Data);
        }
        bool operator!=(const String& value)
        {
            return !(*this == value);
        }
        friend OStream& operator<<(OStream& stream, SpecialStringValue<SpecialStringClass>& value)
        {
            value.EnsureData();
            stream << (String&)(value.Data);
            return stream;
        }
};

class BinaryValue: public Model
{
    protected:
        virtual void CheckValid() const
        {
            if((BinaryData&)Data == (BinaryData&)Data)
            {
            }
        }
    public:
        static const unsigned int MaxSize = 0xFFFF;
        BinaryValue(Value& data)
            : Model(data)
        {
            if(Data.valid() && (BinaryData&)Data == (BinaryData&)Data)
            {
            }
        }
        operator const BinaryData&()
        {
            return (BinaryData&)Data;
        }
        BinaryValue& operator=(const BinaryData& value)
        {
            BinaryData& data = (BinaryData&)Data;
            if(&data == &value)
            {
                BinaryData temp = value;
                data = temp;
            } else
                data = value;
            return *this;
        }
        BinaryValue& operator=(const String& value)
        {
            if(value.size() > MaxSize)
                ERROR(Exception::Types::Binary, "");
            Assign(value.c_str(), (int)value.size());
            return *this;
        }
        BinaryValue& operator=(BinaryValue& value)
        {
            *this = (const BinaryData&)value;
            return *this;
        }
        void Assign(const void* data, int size)
        {
            CheckValid();
            Data = Value(data, size);
        }
        void ToString(String& result)
        {
            const BinaryData& data = *this;
            std::ostringstream stream;
            ENUM_STL_CONST(BinaryData, data, e)
            {
                stream << *e;
            }
            result = stream.str();
        }
};

class Int64Value: public Model
{
    private:
        int64_t FValue;
        Value& FHigh;
        Value& FLow;
    public:
        Int64Value(Value& data)
            : Model(data)
            , FHigh(data[1])
            , FLow(data[0])
        {
            if(FHigh.valid() && (int&)FHigh == (int&)FHigh)
            {
            }
            if(FLow.valid() && (int&)FLow == (int&)FLow)
            {
            }
        }
        operator const int64_t&()
        {
            FValue = (int&)FHigh;
            FValue <<= 32;
            FValue |= (int&)FLow;
            return FValue;
        }
        Int64Value& operator=(int64_t value)
        {
            (int&)FHigh = (int)(value >> 32);
            (int&)FLow = (int)(value & 0xFFFFFFFF);
            FValue = value;
            return *this;
        }
        Int64Value& operator=(Int64Value& value)
        {
            *this = (const int64_t&)value;
            return *this;
        }
        bool operator==(const Int64Value& value)
        {
            return (const int64_t&)value == (const int64_t&)(*this);
        }
        bool operator!=(const Int64Value& value)
        {
            return !(*this == value);
        }
        friend OStream& operator<<(OStream& stream, Int64Value& value)
        {
            stream << (const int64_t&)(value);
            return stream;
        }
};

template<class Element, class ElementIterator>
class ListIterator
{
    private:
        class ItemBuffer
        {
            private:
                Element* FPointer;
            public:
                ItemBuffer()
                    : FPointer(NULL)
                {}
                ~ItemBuffer()
                {
                    Clear();
                }
                Element& Item()
                {
                    return *FPointer;
                }
                Element& Item(Value& value)
                {
                    if(FPointer && &FPointer->Data == &value)
                        return *FPointer;
                    else {
                        Clear();
                        FPointer = new Element(value);
                        return *FPointer;
                    }
                }
                void Clear()
                {
                    if(FPointer)
                    {
                        delete FPointer;
                        FPointer = NULL;
                    }
                }
        };
    private:
        ValueArray* FArray;
        ElementIterator FPos;
        ItemBuffer FBuffer;
    public:
        ListIterator(ValueArray& array, ElementIterator pos)
            : FArray(&array)
            , FPos(pos)
        {}
        bool operator==(const ListIterator<Element, ElementIterator>& iterator) const
        {
            return FPos == iterator.FPos;
        }
        bool operator!=(const ListIterator<Element, ElementIterator>& iterator) const
        {
            return FPos != iterator.FPos;
        }
        ListIterator<Element, ElementIterator> operator++(int)
        {
            FBuffer.Clear();
            return ListIterator<Element, ElementIterator>(*FArray, FPos++);
        }
        ListIterator<Element, ElementIterator> operator++()
        {
            FBuffer.Clear();
            return ListIterator<Element, ElementIterator>(*FArray, ++FPos);
        }
        ListIterator<Element, ElementIterator> operator--(int)
        {
            FBuffer.Clear();
            return ListIterator<Element, ElementIterator>(*FArray, FPos--);
        }
        ListIterator<Element, ElementIterator> operator--()
        {
            FBuffer.Clear();
            return ListIterator<Element, ElementIterator>(*FArray, --FPos);
        }
        ListIterator<Element, ElementIterator>& operator=(const ListIterator<Element, ElementIterator>& iterator)
        {
            FBuffer.Clear();
            FArray = iterator.FArray;
            FPos = iterator.FPos;
            return *this;
        }
        Element& operator*()
        {
            return FBuffer.Item(*FPos);
        }
        Element* operator->()
        {
            return &FBuffer.Item(*FPos);
        }
};

template<class Element>
class List: public Model
{
    protected:
        virtual void CheckValid() const
        {
            Data.getArray();
        }
    private:
        static void OutOfRange(int index)
        {
            ERROR(Exception::Types::RangedList, index);
        }
        void EnsureList()
        {
            if(Data.getType() != Value::TypeArray)
                Data.setSize(0);
        }
        void CheckIndexRange(int index)
        {
            if(index < 0 || index >= Data.size())
                OutOfRange(index);
        }
    public:
        List(Value& list)
            : Model(list)
        {
            if(Data.valid())
                EnsureList();
        }
        Element Head()
        {
            return Get(0);
        }
        Element Last()
        {
            return Get(GetCount() - 1);
        }
        Element Get(int index)
        {
            CheckIndexRange(index);
            return Element(Data[index]);
        }
        int GetCount()
        {
            EnsureList();
            return Data.size();
        }
        Element Append()
        {
            Insert(GetCount());
            return Element(Data[GetCount()-1]);
        }
        void Insert(int index)
        {
            EnsureList();
            const int count = GetCount();
            if(index < 0 || index > count)
                OutOfRange(index);
            Data.setSize(count + 1);
            for(int i = count; i > index; --i)
                Data[i] = Data[i-1];
            Data[index].clear();
        }
        void Delete(int index)
        {
            EnsureList();
            CheckIndexRange(index);
            const int count = GetCount();
            for(int i = index; i < count - 1; ++i)
                Data[i] = Data[i+1];
            Data.setSize(count - 1);
        }
        void Move(int from, int to)
        {
            EnsureList();
            CheckIndexRange(from);
            CheckIndexRange(to);
            if(from < to)
            {
                Value temp = Data[from];
                for(int i = from; i < to; i++)
                    Data[i] = Data[i+1];
                Data[to] = temp;
            } else if(from > to)
            {
                Value temp = Data[from];
                for(int i = from; i > to; i--)
                    Data[i] = Data[i-1];
                Data[to] = temp;
            }
        }
        void Swap(int from, int to)
        {
            EnsureList();
            CheckIndexRange(from);
            CheckIndexRange(to);
            if(from != to)
            {
                Value temp = Data[from];
                Data[from] = Data[to];
                Data[to] = temp;
            }
        }
        void Clear()
        {
            Data.clear();
            EnsureList();
        }
        bool IsEmpty()
        {
            return GetCount() == 0;
        }
        void SetSize(int size)
        {
            Data.setSize(size);
        }
        bool operator==(const List<Element>& list) const
        {
            return list.Data == Data;
        }
        bool operator!=(const List<Element>& list) const
        {
            return !(*this == list);
        }
        List<Element>& operator=(List<Element>& value)
        {
            value.Valid();
            Data = value.Data;
            return *this;
        }
    public:
        typedef ListIterator<Element, ValueArray::iterator> iterator;
        iterator begin()
        {
            return iterator(Data.getArray(), Data.getArray().begin());
        }
        iterator end()
        {
            return iterator(Data.getArray(), Data.getArray().end());
        }
        typedef ListIterator<Element, ValueArray::reverse_iterator> reverse_iterator;
        reverse_iterator rbegin()
        {
            return reverse_iterator(Data.getArray(), Data.getArray().rbegin());
        }
        reverse_iterator rend()
        {
            return reverse_iterator(Data.getArray(), Data.getArray().rend());
        }
};

template<class Element>
class GetListItem: public Model
{
    public:
        BoolValue All;
        UnsignedValue Start;
        UnsignedValue Count;
        List<Element> Result;
    public:
        GetListItem(Value& value)
            : Model(value)
            , All(value["All"])
            , Start(value["Start"])
            , Count(value["Count"])
            , Result(value["Result"])
        {}
        void Ready(const int size, int& start, int& end)
        {
            if(!All.Valid() && !Start.Valid() && !Count.Valid())
                ERROR(Exception::Server::Params, "GetListItem");
            if(!All)
            {
                if(Start >= size)
                    ERROR(Exception::Types::GetList::Start, Start);
                if(Start + Count > size)
                    ERROR(Exception::Types::GetList::Count, Start);
            }
            Result.Clear();
            if(All)
            {
                start = 0;
                end = size;
            } else {
                start = Start;
                end = Count ? Start + Count : size;
            }
        }
};

class ExceptionModel: public Model
{
    public:
        IntValue Code;
        StringValue Message;
    public:
        ExceptionModel(Value& error)
            : Model(error)
            , Code(error["faultCode"])
            , Message(error["faultString"])
        {}
};

#endif // __MODEL_H__
