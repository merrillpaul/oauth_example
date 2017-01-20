import qs from 'qs';

function urlstringify(json) {
  return qs.stringify(json);
}

export {urlstringify};
