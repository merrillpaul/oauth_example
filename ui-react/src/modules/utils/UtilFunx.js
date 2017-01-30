import qs from 'qs';
import React from 'react';
import { keys } from 'lodash';

let forEachChild;
function urlstringify(json) {
  return qs.stringify(json);
}

function deepForEach(node, handler) {
    handler(node);
    if (node.props.children) {
      forEachChild(node.props.children, handler, node);
    }
};

forEachChild = function(children, handler, parent) {
    React.Children.forEach(children, (child) => {
      handler(child, parent);
      if (child.props && child.props.children) {
        forEachChild(child.props.children, handler, child);
      }
    });
};

function excludeProps(exclude, source) {
    var result = {};

    if (source) {
      let srcKeys = keys(source);
      let len = srcKeys.length
      let i = 0;
      for (; i< len; i++) {
        let key = srcKeys[i];
        if (exclude.indexOf(key) === -1) {
          result[key] = source[key];
        }
      }    
    }

    return result;
};

export {urlstringify, deepForEach, excludeProps};
