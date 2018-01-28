import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Tags } from './tags.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TagsService {

    private resourceUrl =  SERVER_API_URL + 'api/tags';
    private resourceUrlUserTags =  SERVER_API_URL + 'api/tagsUsuario';

    constructor(private http: Http) { }

    create(tags: Tags): Observable<Tags> {
        const copy = this.convert(tags);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(tags: Tags): Observable<Tags> {
        const copy = this.convert(tags);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Tags> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    findTags(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrlUserTags, options)
            .map((res: Response) => this.convertResponse(res));
    }

    deleteTagUs(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrlUserTags}/${id}`);
    }

    addTagsUsuario(id: number): Observable<Response> {
        return this.http.get(`${this.resourceUrlUserTags}/add/${id}`);
    }

    /*addTagsUsuario(tags: Tags): Observable<Tags> {
        const copy = this.convert(tags);
        return this.http.post(`${this.resourceUrlUserTags}`, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }*/

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Tags.
     */
    private convertItemFromServer(json: any): Tags {
        const entity: Tags = Object.assign(new Tags(), json);
        return entity;
    }

    /**
     * Convert a Tags to a JSON which can be sent to the server.
     */
    private convert(tags: Tags): Tags {
        const copy: Tags = Object.assign({}, tags);
        return copy;
    }
}