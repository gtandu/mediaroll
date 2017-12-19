import { Component, OnInit} from '@angular/core';
import { Media } from '../../models/media';
import { MediaService } from '../../services/media/media.service';
import { AuthentificationService } from '../../services/authentification.service';

@Component({
  selector: 'app-medialist',
  templateUrl: './media-list.component.html',
  styleUrls: ['./media-list.component.css']
})
export class MediaListComponent implements OnInit {
  medias: Media[] = [];

  constructor(private mediaService: MediaService) {}

  ngOnInit() {
    this.mediaService.getAllMedias().subscribe(medias => {
      this.medias = medias;
    });

  }


  ngAfterViewChecked() {
    ($(".materialboxed") as any).materialbox();
  }

  mediaUrl(id: string) {
    return this.mediaService.buildUrlToMediaWithId(id);
  }
}
