/**
 * Sets a value of nested key string descriptor inside a Object.
 * It changes the passed object.
 * Ex:
 *    let obj = {a: {b:{c:'initial'}}}
 *    setNestedKey(obj, ['a', 'b', 'c'], 'changed-value')
 *    assert(obj === {a: {b:{c:'changed-value'}}})
 *
 * @param {object} obj   Object to set the nested key
 * @param {string[]} path  An array to describe the path(Ex: ['a', 'b', 'c'])
 * @param {object} value Any value
 */
export const setNestedKey = (obj, path, value) => {
    if (path.length === 1) {
        obj[path] = value
        return
    }
    return setNestedKey(obj[path[0]], path.slice(1), value)
}

/**
 * Gets a value of nested key string descriptor inside a Object.
 * Ex:
 *    let obj = {a: {b:{c:'initial'}}}
 *    getNestedValue(obj, ['a', 'b'])
 *    assert(obj === {c:'changed-value'})
 *
 * @param {object} obj   Object to get the nested value
 * @param {string[]} path  An array to describe the path(Ex: ['a', 'b'])
 */
export const getNestedValue = (obj, path) => {
    if (path.length === 1) {
        return obj[path];
    }
    return getNestedValue(obj[path[0]], path.slice(1))
}