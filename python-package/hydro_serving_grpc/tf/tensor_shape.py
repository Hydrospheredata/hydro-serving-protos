from hydro_serving_grpc import TensorShapeProto


def shape_from_proto(tensor_shape_proto):
    """
    Converts TensorShapeProto to a list of dims.
    :param tensor_shape_proto: TensorShapeProto object. Could be None
    :return: None if tensor_shape_proto is None, list of ints otherwise
    """
    if tensor_shape_proto is None:
        return None
    if not isinstance(tensor_shape_proto, TensorShapeProto):
        raise TypeError()

    shape_list = [x.size for x in tensor_shape_proto.dim]
    return shape_list


def shape_to_proto(dim_list):
    """
    Creates a TensorShapeProto object from list of dims.
    :param dim_list: could be None
    :return: None if dim_list is None, TensorShapeProto otherwise
    """

    if dim_list is None:
        return None
    pshape = TensorShapeProto()
    dims = [TensorShapeProto.Dim(size=dim) for dim in dim_list]
    pshape.dim.extend(dims)
    return pshape
