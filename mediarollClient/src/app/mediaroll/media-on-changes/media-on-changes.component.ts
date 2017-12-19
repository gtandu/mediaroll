import { Component, OnInit, OnChanges, SimpleChanges, SimpleChange, Input } from '@angular/core';
import { Media } from '../../models/media';
import { MediaService } from '../../services/media/media.service';
@Component({
  selector: 'app-media-on-changes',
  templateUrl: './media-on-changes.component.html',
  styleUrls: ['./media-on-changes.component.css']
})
export class MediaOnChangesComponent implements OnInit, OnChanges {

  @Input() mediasUploaded: Media[];

  constructor(private mediaService: MediaService) { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges) {
  }

  ngAfterViewChecked() {
    ($(".materialboxed") as any).materialbox();
  }

  mediaUrl(id: string) {
    return this.mediaService.buildUrlToMediaWithId(id);
  }

}
