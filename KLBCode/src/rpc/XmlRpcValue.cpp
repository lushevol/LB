#include "XmlRpcValue.h"
#include "XmlRpcException.h"
#include "XmlRpcUtil.h"
#include "base64.h"

#ifndef MAKEDEPEND
# include <memory>
# include <iostream>
# include <ostream>
# include <stdlib.h>
# include <stdio.h>
#endif

namespace XmlRpc {


    static const char VALUE_TAG[]           = "<value>";
    static const char VALUE_ETAG[]          = "</value>";

    static const char BOOLEAN_TAG[]         = "<boolean>";
    static const char BOOLEAN_ETAG[]        = "</boolean>";
    static const char DOUBLE_TAG[]          = "<double>";
    static const char DOUBLE_ETAG[]         = "</double>";
    static const char INT_TAG[]             = "<int>";
    static const char I4_TAG[]              = "<i4>";
    static const char I4_ETAG[]             = "</i4>";
    static const char STRING_TAG[]          = "<string>";
    static const char STRING_ETAG[]         = "</string>";
    static const char DATETIME_TAG[]        = "<dateTime.iso8601>";
    static const char DATETIME_ETAG[]       = "</dateTime.iso8601>";
    static const char BASE64_TAG[]          = "<base64>";
    static const char BASE64_ETAG[]         = "</base64>";

    static const char ARRAY_TAG[]           = "<array>";
    static const char DATA_TAG[]            = "<data>";
    static const char DATA_ETAG[]           = "</data>";
    static const char ARRAY_ETAG[]          = "</array>";

    static const char STRUCT_TAG[]          = "<struct>";
    static const char MEMBER_TAG[]          = "<member>";
    static const char NAME_TAG[]            = "<name>";
    static const char NAME_ETAG[]           = "</name>";
    static const char MEMBER_ETAG[]         = "</member>";
    static const char STRUCT_ETAG[]         = "</struct>";



    // Format strings
    std::string XmlRpcValue::_doubleFormat("%f");



    // Clean up
    void XmlRpcValue::invalidate()
    {
        switch(_type) {
            case TypeString:
                delete _value.asString;
                break;
            case TypeDateTime:
                delete _value.asTime;
                break;
            case TypeBase64:
                delete _value.asBinary;
                break;
            case TypeArray:
                delete _value.asArray;
                break;
            case TypeStruct:
                delete _value.asStruct;
                break;
            default:
                break;
        }
        _type = TypeInvalid;
        _value.asBinary = 0;
    }


    // Type checking
    void XmlRpcValue::assertTypeOrInvalid(Type t)
    {
        if(_type == TypeInvalid)
        {
            _type = t;
            switch(_type) {     // Ensure there is a valid value for the type
                case TypeString:
                    _value.asString = new std::string();
                    break;
                case TypeDateTime:
                    _value.asTime = new struct tm();
                    break;
                case TypeBase64:
                    _value.asBinary = new BinaryData();
                    break;
                case TypeArray:
                    _value.asArray = new ValueArray();
                    break;
                case TypeStruct:
                    _value.asStruct = new ValueStruct();
                    break;
                default:
                    _value.asBinary = 0;
                    break;
            }
        }
        else if(_type != t)
            throw XmlRpcException("type error");
    }

    void XmlRpcValue::assertTypeOrInvalid(Type t) const
    {
        if(_type != t)
            throw XmlRpcException("type error");
    }

    void XmlRpcValue::assertArray(int size) const
    {
        if(_type != TypeArray)
            throw XmlRpcException("type error: expected an array");
        else if(int(_value.asArray->size()) < size)
            throw XmlRpcException("range error: array index too large");
    }


    void XmlRpcValue::assertArray(int size)
    {
        if(_type == TypeInvalid) {
            _type = TypeArray;
            _value.asArray = new ValueArray(size);
        } else if(_type == TypeArray) {
            if(int(_value.asArray->size()) < size)
                _value.asArray->resize(size);
        } else
            throw XmlRpcException("type error: expected an array");
    }

    void XmlRpcValue::assertStruct()
    {
        if(_type == TypeInvalid) {
            _type = TypeStruct;
            _value.asStruct = new ValueStruct();
        } else if(_type != TypeStruct)
            throw XmlRpcException("type error: expected a struct");
    }

    void XmlRpcValue::assertStruct() const
    {
        if(_type != TypeStruct)
            throw XmlRpcException("type error: expected a struct");
    }

    // Operators
    XmlRpcValue& XmlRpcValue::operator=(XmlRpcValue const& rhs)
    {
        if(this != &rhs)
        {
            invalidate();
            _type = rhs._type;
            switch(_type) {
                case TypeBoolean:
                    _value.asBool = rhs._value.asBool;
                    break;
                case TypeInt:
                    _value.asInt = rhs._value.asInt;
                    break;
                case TypeDouble:
                    _value.asDouble = rhs._value.asDouble;
                    break;
                case TypeDateTime:
                    _value.asTime = new struct tm(*rhs._value.asTime);
                    break;
                case TypeString:
                    _value.asString = new std::string(*rhs._value.asString);
                    break;
                case TypeBase64:
                    _value.asBinary = new BinaryData(*rhs._value.asBinary);
                    break;
                case TypeArray:
                    _value.asArray = new ValueArray(*rhs._value.asArray);
                    break;
                case TypeStruct:
                    _value.asStruct = new ValueStruct(*rhs._value.asStruct);
                    break;
                default:
                    _value.asBinary = 0;
                    break;
            }
        }
        return *this;
    }


    // Predicate for tm equality
    static bool tmEq(struct tm const& t1, struct tm const& t2) {
        return t1.tm_sec == t2.tm_sec && t1.tm_min == t2.tm_min &&
               t1.tm_hour == t2.tm_hour && t1.tm_mday == t1.tm_mday &&
               t1.tm_mon == t2.tm_mon && t1.tm_year == t2.tm_year;
    }

    bool XmlRpcValue::operator==(XmlRpcValue const& other) const
    {
        if(_type != other._type)
            return false;

        switch(_type) {
            case TypeBoolean:
                return (!_value.asBool && !other._value.asBool) ||
                       (_value.asBool && other._value.asBool);
            case TypeInt:
                return _value.asInt == other._value.asInt;
            case TypeDouble:
                return _value.asDouble == other._value.asDouble;
            case TypeDateTime:
                return tmEq(*_value.asTime, *other._value.asTime);
            case TypeString:
                return *_value.asString == *other._value.asString;
            case TypeBase64:
                return *_value.asBinary == *other._value.asBinary;
            case TypeArray:
                return *_value.asArray == *other._value.asArray;

                // The map<>::operator== requires the definition of value< for kcc
            case TypeStruct:   //return *_value.asStruct == *other._value.asStruct;
                {
                    if(_value.asStruct->size() != other._value.asStruct->size())
                        return false;

                    ValueStruct::const_iterator it1 = _value.asStruct->begin();
                    ValueStruct::const_iterator it2 = other._value.asStruct->begin();
                    while(it1 != _value.asStruct->end()) {
                        const XmlRpcValue& v1 = it1->second;
                        const XmlRpcValue& v2 = it2->second;
                        if(!(v1 == v2))
                            return false;
                        it1++;
                        it2++;
                    }
                    return true;
                }
            default:
                break;
        }
        return true;    // Both invalid values ...
    }

    bool XmlRpcValue::operator!=(XmlRpcValue const& other) const
    {
        return !(*this == other);
    }


    // Works for strings, binary data, arrays, and structs.
    int XmlRpcValue::size() const
    {
        switch(_type) {
            case TypeString:
                return int(_value.asString->size());
            case TypeBase64:
                return int(_value.asBinary->size());
            case TypeArray:
                return int(_value.asArray->size());
            case TypeStruct:
                return int(_value.asStruct->size());
            default:
                break;
        }

        throw XmlRpcException("type error");
    }

    // Checks for existence of struct member
    bool XmlRpcValue::hasMember(const std::string& name) const
    {
        return _type == TypeStruct && _value.asStruct->find(name) != _value.asStruct->end();
    }

    // Set the value from xml. The chars at *offset into valueXml
    // should be the start of a <value> tag. Destroys any existing value.
    bool XmlRpcValue::fromXml(std::string const& valueXml)
    {
        invalidate();
        std::auto_ptr<TiXmlDocument> doc(new TiXmlDocument());
        doc->Parse(valueXml.c_str());
        return fromXml(doc->RootElement());
    }

    bool XmlRpcValue::fromXml(TiXmlElement* value)
    {
        invalidate();
        if(value == NULL)
            return false;
        if(value->ValueStr() != "value")
            return false;
        TiXmlElement* type = value->FirstChildElement();
        if(type == NULL || type->ValueStr() == "string")
        {
            return stringFromXml((type ? type : value)->GetText());
        } else if(type->ValueStr() == "boolean")
        {
            return boolFromXml(type->GetText());
        } else if(type->ValueStr() == "double")
        {
            return doubleFromXml(type->GetText());
        } else if(type->ValueStr() == "i4")
        {
            return intFromXml(type->GetText());
        } else if(type->ValueStr() == "dateTime.iso8601")
        {
            return timeFromXml(type->GetText());
        } else if(type->ValueStr() == "base64")
        {
            return binaryFromXml(type->GetText());
        } else if(type->ValueStr() == "array")
        {
            return arrayFromXml(type);
        } else if(type->ValueStr() == "struct")
        {
            return structFromXml(type);
        } else
            return false;
    }

    // Encode the Value in xml
    std::string XmlRpcValue::toXml() const
    {
        switch(_type) {
            case TypeBoolean:
                return boolToXml();
            case TypeInt:
                return intToXml();
            case TypeDouble:
                return doubleToXml();
            case TypeString:
                return stringToXml();
            case TypeDateTime:
                return timeToXml();
            case TypeBase64:
                return binaryToXml();
            case TypeArray:
                return arrayToXml();
            case TypeStruct:
                return structToXml();
            default:
                break;
        }
        return std::string();   // Invalid value
    }


    // Boolean
    bool XmlRpcValue::boolFromXml(const char* value)
    {
        if(!value)
            return false;
        char* valueEnd;
        long ivalue = strtol(value, &valueEnd, 10);
        if(valueEnd == value || (ivalue != 0 && ivalue != 1))
            return false;

        _type = TypeBoolean;
        _value.asBool = (ivalue == 1);
        return true;
    }

    std::string XmlRpcValue::boolToXml() const
    {
        std::string xml = VALUE_TAG;
        xml += BOOLEAN_TAG;
        xml += (_value.asBool ? "1" : "0");
        xml += BOOLEAN_ETAG;
        xml += VALUE_ETAG;
        return xml;
    }

    // Int
    bool XmlRpcValue::intFromXml(const char* value)
    {
        if(!value)
            return false;
        char* valueEnd;
        long ivalue = strtol(value, &valueEnd, 10);
        if(valueEnd == value)
            return false;

        _type = TypeInt;
        _value.asInt = int(ivalue);
        return true;
    }

    std::string XmlRpcValue::intToXml() const
    {
        char buf[256];
        snprintf(buf, sizeof(buf) - 1, "%d", _value.asInt);
        buf[sizeof(buf)-1] = 0;
        std::string xml = VALUE_TAG;
        xml += I4_TAG;
        xml += buf;
        xml += I4_ETAG;
        xml += VALUE_ETAG;
        return xml;
    }

    // Double
    bool XmlRpcValue::doubleFromXml(const char* value)
    {
        if(!value)
            return false;
        char* valueEnd;
        double dvalue = strtod(value, &valueEnd);
        if(valueEnd == value)
            return false;

        _type = TypeDouble;
        _value.asDouble = dvalue;
        return true;
    }

    std::string XmlRpcValue::doubleToXml() const
    {
        char buf[256];
        snprintf(buf, sizeof(buf) - 1, getDoubleFormat().c_str(), _value.asDouble);
        buf[sizeof(buf)-1] = 0;

        std::string xml = VALUE_TAG;
        xml += DOUBLE_TAG;
        xml += buf;
        xml += DOUBLE_ETAG;
        xml += VALUE_ETAG;
        return xml;
    }

    // String
    bool XmlRpcValue::stringFromXml(const char* value)
    {
        _type = TypeString;
        if(value)
            _value.asString = new std::string(value);//Do not need xmlDecode, use TinyXml instead.
        else
            _value.asString = new std::string();
        return true;
    }

    std::string XmlRpcValue::stringToXml() const
    {
        std::string xml = VALUE_TAG;
        //xml += STRING_TAG; optional
        xml += XmlRpcUtil::xmlEncode(*_value.asString);
        //xml += STRING_ETAG;
        xml += VALUE_ETAG;
        return xml;
    }

    // DateTime (stored as a struct tm)
    bool XmlRpcValue::timeFromXml(const char* value)
    {
        if(!value)
            return false;

        std::string stime(value);

        struct tm t;
        if(sscanf(stime.c_str(), "%4d%2d%2dT%2d:%2d:%2d", &t.tm_year, &t.tm_mon, &t.tm_mday, &t.tm_hour, &t.tm_min, &t.tm_sec) != 6)
            return false;

        t.tm_isdst = -1;
        _type = TypeDateTime;
        _value.asTime = new struct tm(t);
        return true;
    }

    std::string XmlRpcValue::timeToXml() const
    {
        struct tm* t = _value.asTime;
        char buf[20];
        snprintf(buf, sizeof(buf) - 1, "%4d%02d%02dT%02d:%02d:%02d",
                 t->tm_year, t->tm_mon, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
        buf[sizeof(buf)-1] = 0;

        std::string xml = VALUE_TAG;
        xml += DATETIME_TAG;
        xml += buf;
        xml += DATETIME_ETAG;
        xml += VALUE_ETAG;
        return xml;
    }


    // Base64
    bool XmlRpcValue::binaryFromXml(const char* value)
    {
        _type = TypeBase64;
        std::string asString(value ? value : "");
        _value.asBinary = new BinaryData();
        // check whether base64 encodings can contain chars xml encodes...

        // convert from base64 to binary
        int iostatus = 0;
        base64<char> decoder;
        std::back_insert_iterator<BinaryData> ins = std::back_inserter(*(_value.asBinary));
        decoder.get(asString.begin(), asString.end(), ins, iostatus);

        return true;
    }


    std::string XmlRpcValue::binaryToXml() const
    {
        // convert to base64
        std::vector<char> base64data;
        int iostatus = 0;
        base64<char> encoder;
        std::back_insert_iterator<std::vector<char> > ins = std::back_inserter(base64data);
        encoder.put(_value.asBinary->begin(), _value.asBinary->end(), ins, iostatus, base64<>::crlf());

        // Wrap with xml
        std::string xml = VALUE_TAG;
        xml += BASE64_TAG;
        xml.append(base64data.begin(), base64data.end());
        xml += BASE64_ETAG;
        xml += VALUE_ETAG;
        return xml;
    }


    // Array
    bool XmlRpcValue::arrayFromXml(TiXmlElement* value)
    {
        TiXmlElement* data = value->FirstChildElement("data");
        if(!data)
            return false;

        _type = TypeArray;
        _value.asArray = new ValueArray;
        TiXmlNode* iterator = NULL;
        while((iterator = data->IterateChildren(iterator)))
        {
            if(TiXmlElement* subvalue = iterator->ToElement())
            {
                XmlRpcValue v;
                if(v.fromXml(subvalue))
                    _value.asArray->push_back(v);
            }
        }
        return true;
    }


    // In general, its preferable to generate the xml of each element of the
    // array as it is needed rather than glomming up one big string.
    std::string XmlRpcValue::arrayToXml() const
    {
        std::string xml = VALUE_TAG;
        xml += ARRAY_TAG;
        xml += DATA_TAG;

        int s = int(_value.asArray->size());
        for(int i = 0; i < s; ++i)
            xml += _value.asArray->at(i).toXml();

        xml += DATA_ETAG;
        xml += ARRAY_ETAG;
        xml += VALUE_ETAG;
        return xml;
    }


    // Struct
    bool XmlRpcValue::structFromXml(TiXmlElement* value)
    {
        _type = TypeStruct;
        _value.asStruct = new ValueStruct;
        TiXmlNode* iterator = NULL;
        while((iterator = value->IterateChildren(iterator)))
        {
            if(TiXmlElement* data = iterator->ToElement())
            {
                if(data->ValueStr() != "member")
                    return false;
                TiXmlElement* name = data->FirstChildElement("name");
                if(!name)
                    return false;
                TiXmlElement* subvalue = data->FirstChildElement("value");
                if(!subvalue)
                    return false;
                XmlRpcValue v;
                if(v.fromXml(subvalue))
                {
                    const std::pair<const std::string, XmlRpcValue> p(name->GetText() ? name->GetText() : "", v);
                    _value.asStruct->insert(p);
                }
            }
        }
        return true;
    }


    // In general, its preferable to generate the xml of each element
    // as it is needed rather than glomming up one big string.
    std::string XmlRpcValue::structToXml() const
    {
        std::string xml = VALUE_TAG;
        xml += STRUCT_TAG;

        ValueStruct::const_iterator it;
        for(it = _value.asStruct->begin(); it != _value.asStruct->end(); ++it) {
            if(it->second.valid()) {
                xml += MEMBER_TAG;
                xml += NAME_TAG;
                xml += XmlRpcUtil::xmlEncode(it->first);
                xml += NAME_ETAG;
                xml += it->second.toXml();
                xml += MEMBER_ETAG;
            }
        }

        xml += STRUCT_ETAG;
        xml += VALUE_ETAG;
        return xml;
    }



    // Write the value without xml encoding it
    std::ostream& XmlRpcValue::write(std::ostream& os) const {
        switch(_type) {
            default:
                break;
            case TypeBoolean:
                os << _value.asBool;
                break;
            case TypeInt:
                os << _value.asInt;
                break;
            case TypeDouble:
                os << _value.asDouble;
                break;
            case TypeString:
                os << *_value.asString;
                break;
            case TypeDateTime:
                {
                    struct tm* t = _value.asTime;
                    char buf[20];
                    snprintf(buf, sizeof(buf) - 1, "%4d%02d%02dT%02d:%02d:%02d",
                             t->tm_year, t->tm_mon, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
                    buf[sizeof(buf)-1] = 0;
                    os << buf;
                    break;
                }
            case TypeBase64:
                {
                    int iostatus = 0;
                    std::ostreambuf_iterator<char> out(os);
                    base64<char> encoder;
                    encoder.put(_value.asBinary->begin(), _value.asBinary->end(), out, iostatus, base64<>::crlf());
                    break;
                }
            case TypeArray:
                {
                    int s = int(_value.asArray->size());
                    os << '{';
                    for(int i = 0; i < s; ++i)
                    {
                        if(i > 0) os << ',';
                        _value.asArray->at(i).write(os);
                    }
                    os << '}';
                    break;
                }
            case TypeStruct:
                {
                    os << '[';
                    ValueStruct::const_iterator it;
                    for(it = _value.asStruct->begin(); it != _value.asStruct->end(); ++it)
                    {
                        if(it != _value.asStruct->begin()) os << ',';
                        os << it->first << ':';
                        it->second.write(os);
                    }
                    os << ']';
                    break;
                }

        }

        return os;
    }

} // namespace XmlRpc


// ostream
std::ostream& operator<<(std::ostream& os, XmlRpc::XmlRpcValue& v)
{
    // If you want to output in xml format:
    //return os << v.toXml();
    return v.write(os);
}

