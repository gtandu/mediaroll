export class Paths{

    public static readonly GET_TOKEN = '/api-token';
    public static readonly MEDIAS = '/medias';
    public static readonly MEDIA_ID = '/{mediaId}';
    public static readonly MEDIAS_WITH_ID = Paths.MEDIAS + Paths.MEDIA_ID;
    public static readonly MEDIA_RESPONSE = Paths.MEDIAS_WITH_ID + '/response';

}