/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * Parse the time to string
 * @param {(Object|string|number)} time
 * @param {string} cFormat
 * @returns {string | null}
 */
export function parseTime(time, cFormat) {
  if (arguments.length === 0 || !time) {
    return null;
  }

  const format = cFormat || '{y}年{m}月{d}日 {h}:{i}:{s}';
  let date;

  if (typeof time === 'object') {
    date = time;
  } else {
    if (typeof time === 'string') {
      const parts = time.split('T');
      if (parts.length === 2) {
        const datePart = parts[0];
        const timePart = parts[1];

        // Parse date part
        if (/^[0-9]{4}-[0-1][0-9]-[0-3][0-9]$/.test(datePart)) {
          date = new Date(datePart);
        } else {
          return null; // Invalid date format
        }

        // Parse time part with milliseconds and timezone offset
        const timeParts = timePart.split('.');
        if (timeParts.length > 1 && timeParts.length < 4) {
          const timeStr = timeParts[0];
          const milliseconds = timeParts.length === 2 ? timeParts[1] : '000';
          const offset = time.slice(-6); // Extract last 6 characters for offset

          // Validate time format
          if (/^[0-9]{2}:[0-5][0-9]:[0-5][0-9]$/.test(timeStr)) {
            date.setHours(parseInt(timeStr.split(':')[0]));
            date.setMinutes(parseInt(timeStr.split(':')[1]));
            date.setSeconds(parseInt(timeStr.split(':')[2]));
            date.setMilliseconds(parseInt(milliseconds));

            // Validate and handle timezone offset (+HH:MM or -HH:MM)
            if (/^[\+-]?[0-1][0-9]:[0-5][0-9]$/.test(offset)) {
              const sign = offset[0] === '-' ? -1 : 1;
              const hours = parseInt(offset.slice(1, 3));
              const minutes = parseInt(offset.slice(4));
              date.setUTCHours(date.getUTCHours() + sign * hours);
              date.setUTCMinutes(date.getUTCMinutes() + sign * minutes);
            } else {
              // Invalid timezone offset format
              return null;
            }
          } else {
            return null; // Invalid time format
          }
        } else {
          return null; // Invalid time format with milliseconds
        }
      } else {
        return null; // Invalid format, missing 'T' separator
      }
    } else {
      return null; // Unsupported time type
    }
  }

  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    SSS: date.getMilliseconds().toString().padStart(3, '0'),
    Z: (date.getTimezoneOffset() / -60).toString().padStart(2, '0') + (date.getTimezoneOffset() > 0 ? '-' : '+'),
  };

  const time_str = format.replace(/{([ymdhisaZ]+)}/g, (result, key) => {
    const value = formatObj[key];
    return value.toString().padStart(key.length, '0');
  });

  return time_str;
}


export function parseDuration(duration) {
  const [hours, minutes, seconds] = duration.split(':').map(Number);

  return [
    hours > 0 && `${hours}小时`,
    minutes > 0 && `${minutes}分`,
    seconds > 0 && `${seconds}秒`,
  ].filter(Boolean).join('');
}
// function to calculate local time

// in a different city

// given the city's UTC offset

export function UTC2GMT(UTCDateString) {

  if (!UTCDateString) {
    return '-';
  }
  function formatFunc(str) {    //格式化显示
    return str > 9 ? str : '0' + str
  }
  var date2 = new Date(UTCDateString);     //这步是关键
  var year = date2.getFullYear();
  var mon = formatFunc(date2.getMonth() + 1);
  var day = formatFunc(date2.getDate());
  var hour = date2.getHours();
  var noon = hour >= 12 ? 'PM' : 'AM';
  hour = hour >= 12 ? hour - 12 : hour;
  hour = formatFunc(hour);
  var min = formatFunc(date2.getMinutes());
  var dateStr = year + '-' + mon + '-' + day + ' ' + noon + ' ' + hour + ':' + min;
  return dateStr;
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function param2Obj(url) {
  const search = decodeURIComponent(url.split('?')[1]).replace(/\+/g, ' ')
  if (!search) {
    return {}
  }
  const obj = {}
  const searchArr = search.split('&')
  searchArr.forEach(v => {
    const index = v.indexOf('=')
    if (index !== -1) {
      const name = v.substring(0, index)
      const val = v.substring(index + 1, v.length)
      obj[name] = val
    }
  })
  return obj
}
